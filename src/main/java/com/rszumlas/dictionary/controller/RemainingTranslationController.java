package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.RemainingTranslation;
import com.rszumlas.dictionary.service.RemainingTranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/remaining-translation")
@RestController
public class RemainingTranslationController {

    private final RemainingTranslationService remainingTranslationService;

    @GetMapping
    public List<RemainingTranslation> getAllRemainingTranslations() {
        return remainingTranslationService.getAllRemainingTranslations();
    }

}
