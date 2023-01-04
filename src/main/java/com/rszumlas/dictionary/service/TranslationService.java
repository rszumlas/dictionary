package com.rszumlas.dictionary.service;

import com.rszumlas.dictionary.exception.ApiRequestException;
import com.rszumlas.dictionary.model.RemainingTranslation;
import com.rszumlas.dictionary.model.Translation;
import com.rszumlas.dictionary.repository.RemainingTranslationRepository;
import com.rszumlas.dictionary.repository.TranslationRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final RemainingTranslationRepository remainingTranslationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TranslationService.class);

    // getTranslation
    public String getTranslation(String word) {
        Translation foundTranslation = translationRepository.findTranslationByWord(word)
                .orElseThrow(() -> new ApiRequestException("Translation not found", HttpStatus.NOT_FOUND));
        return getTranslatedWord(word, foundTranslation);
    }

    protected String getTranslatedWord(String word, Translation foundTranslation) {
        String translatedWord = null;
        if (foundTranslation.getPolishWord().equals(word)) {
            translatedWord = foundTranslation.getEnglishWord();
            LOGGER.info(String.format("%s found in polish dictionary", word));
        }
        if (foundTranslation.getEnglishWord().equals(word)) {
            translatedWord = foundTranslation.getPolishWord();
            LOGGER.info(String.format("%s found in english dictionary", word));
        }
        return translatedWord;
    }

    // getTranslationPage
    public List<Translation> getTranslationPage(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Translation> translationPage = translationRepository.findAll(pageRequest);
        LOGGER.info(String.format("Page retrieved - %s", translationPage));
        return translationPage.getContent();
    }

    // saveTranslation
    public void saveTranslation(String polishWord, String englishWord) {
        Translation translation = new Translation(null, polishWord, englishWord);
        translationRepository.saveAndFlush(translation);
        LOGGER.info(String.format("Saved translation - %s", translation));
    }

    // translateSentence
    public String getSentenceTranslation(String sentence) {
        List<String> words = castSentenceToListOfWords(sentence);
        for(String w : words) {
            LOGGER.info(String.format("Translating %s", w));
            tryCatchGetTranslation(words, w);
        }
        String translatedSentence = words.toString().replaceAll(",", " ");
        return translatedSentence;
    }

    protected void tryCatchGetTranslation(List<String> words, String w) {
        String translatedWord = w;
        try {
            translatedWord = getTranslation(w);
            LOGGER.info(translatedWord);
            if (!w.equals(translatedWord)) {
                words.set(words.indexOf(w), translatedWord);
                LOGGER.info(String.format("Translated to word %s", w));
            }
        } catch(ApiRequestException e) {
            remainingTranslationRepository.save(new RemainingTranslation(null, w));
            LOGGER.info(String.format("ApiException occured during translating %s word", w));
        }
    }

    protected List<String> castSentenceToListOfWords(String sentence) {
        // "\\s+" - multiple spaces
        String[] words = sentence.split("\\s+");
        return new ArrayList<String>(Arrays.asList(words));
    }

}
