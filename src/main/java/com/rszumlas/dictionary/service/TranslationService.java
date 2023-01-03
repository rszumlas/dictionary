package com.rszumlas.dictionary.service;

import com.rszumlas.dictionary.repository.TranslationRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TranslationService.class);

    public String getTranslation(String word) {
        translationRepository.findTranslationByWord(word);

        return null;
    }

}
