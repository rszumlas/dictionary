package com.rszumlas.dictionary.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.rszumlas.dictionary.exception.ApiRequestException;
import com.rszumlas.dictionary.model.DictionaryReport;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final RemainingTranslationRepository remainingTranslationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TranslationService.class);

    // getSentenceTranslation
    public String getSentenceTranslation(String sentence) {
        List<String> words = castSentenceToListOfWords(sentence);
        for(String w : words) {
            LOGGER.info(String.format("Translating %s", w));
            tryCatchGetTranslation(words, w);
        }
        return words.toString().replaceAll(",", "").replaceAll("[\\[\\]]", "");
    }

    protected List<String> castSentenceToListOfWords(String sentence) {
        // "\\s+" - multiple spaces
        String[] words = sentence.split("\\s+");
        return new ArrayList<>(Arrays.asList(words));
    }

    protected void tryCatchGetTranslation(List<String> words, String w) {
        String translatedWord = w;
        try {
            translatedWord = getWordTranslation(w);
            LOGGER.info(translatedWord);
            if (!w.equals(translatedWord)) {
                words.set(words.indexOf(w), translatedWord);
                LOGGER.info(String.format("Translated to word %s", w));
            }
        } catch(ApiRequestException e) {
            LOGGER.info(String.format("ApiException occurred during translating %s word", w));
            remainingTranslationRepository.save(new RemainingTranslation(null, w));
            LOGGER.info("Remaining translation saved");
        }
    }

    // getWordTranslation
    public String getWordTranslation(String word) {
        Translation foundTranslation = translationRepository.findTranslationByWord(word)
                .orElseThrow(() -> new ApiRequestException("Translation not found", HttpStatus.NOT_FOUND));
        return getTranslatedWord(word, foundTranslation);
    }

    protected String getTranslatedWord(String word, Translation foundTranslation) {
        String translatedWord = null;
        if (foundTranslation.getPolishWord().equalsIgnoreCase(word)) {
            translatedWord = foundTranslation.getEnglishWord();
            LOGGER.info(String.format("%s found in polish dictionary", word));
        }
        if (foundTranslation.getEnglishWord().equalsIgnoreCase(word)) {
            translatedWord = foundTranslation.getPolishWord();
            LOGGER.info(String.format("%s found in english dictionary", word));
        }
        return translatedWord;
    }

    // getPdfReport
    public ByteArrayInputStream getPdfReport() {
        
        LOGGER.info("Pdf creation started");
        String title = "Dictionary Report";
        String report = getDictionaryReport().toString();

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);


        Paragraph titleParagraph = createTitleParagraph(title);
        Paragraph paragraph = createParagraph(FontFactory.HELVETICA, 18, report);

        addParagraphsToDocument(document, List.of(titleParagraph, paragraph));

        return new ByteArrayInputStream(out.toByteArray());
    }

    private Paragraph createTitleParagraph(String title) {
        Paragraph titleParagraph = createParagraph(FontFactory.HELVETICA_BOLD, 26, title);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        return titleParagraph;
    }
    
    private Paragraph createParagraph(String helvetica, int size, String report) {
        Font paragraphFont = FontFactory.getFont(helvetica, size);
        Paragraph paragraph = new Paragraph(report, paragraphFont);
        return paragraph;
    }

    private void addParagraphsToDocument(Document document, List<Paragraph> paragraphs) {
        document.open();
        for (Paragraph p : paragraphs) {
            document.add(p);
        }
        document.close();
    }    

    // getDictionaryReport
    public DictionaryReport getDictionaryReport() {
        String polish = "polish";
        String english = "english";
        DictionaryReport report = new DictionaryReport();

        translationRepository.save(new Translation(1L, "kot", "cat"));
        translationRepository.save(new Translation(2L, "i????", "go"));
        translationRepository.save(new Translation(3L, "pies", "dog"));

        report.setWordCount(translationRepository.count()*2);
        report.setPolishWordsLengthCount(countWordsOfSpecificLength(polish));
        report.setEnglishWordsLengthCount(countWordsOfSpecificLength(english));
        report.setAvgPolishWordLength(averageWordLength(polish));
        report.setAvgEnglishWordLength(averageWordLength(english));

        return report;
    }

    protected Map<Long, Long> countWordsOfSpecificLength(String language) {
        ArrayList<Long> wordsLengths = getWordsLenListByLanguage(language);

        Map<Long, Long> wordsLengthCount = wordsLengths.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        LOGGER.info(wordsLengthCount.toString());
        return wordsLengthCount;
    }

    protected Double averageWordLength(String language) {
        ArrayList<Long> wordsLengths = getWordsLenListByLanguage(language);

        double averageLength = wordsLengths.stream()
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();
        averageLength = BigDecimal.valueOf(averageLength).setScale(1, RoundingMode.HALF_EVEN).doubleValue();
        return averageLength;
    }

    protected ArrayList<Long> getWordsLenListByLanguage(String language) {
        ArrayList<Translation> translations = new ArrayList<>(translationRepository.findAll());
        ArrayList<Long> wordsLengths = new ArrayList<>();

        switch (language) {
            case "polish" -> translations.stream()
                    .map(Translation::getPolishWord)
                    .forEach(t -> wordsLengths.add((long) t.length()));
            case "english" -> translations.stream()
                    .map(Translation::getEnglishWord)
                    .forEach(t -> wordsLengths.add((long) t.length()));
        }

        return wordsLengths;
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
        Translation translation = new Translation(null, polishWord.toLowerCase(), englishWord);
        translationRepository.saveAndFlush(translation);
        LOGGER.info(String.format("Saved translation - %s", translation));
    }

}
