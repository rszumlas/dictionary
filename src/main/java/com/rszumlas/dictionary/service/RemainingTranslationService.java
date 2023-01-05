package com.rszumlas.dictionary.service;

import com.rszumlas.dictionary.model.RemainingTranslation;
import com.rszumlas.dictionary.repository.RemainingTranslationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RemainingTranslationService {

    private final RemainingTranslationRepository remainingTranslationRepository;

    public List<RemainingTranslation> getAllRemainingTranslations() {
        return remainingTranslationRepository.findAll();
    }
}
