package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.RemainingTranslation;
import com.rszumlas.dictionary.service.RemainingTranslationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/remaining-translations")
@RestController
public class RemainingTranslationController {

    private final RemainingTranslationService remainingTranslationService;

    @Operation(
            summary = "Get saved unknown words",
            description = "Fetches all previously saved words that were not recognized during translations."
    )
    @GetMapping
    public List<RemainingTranslation> getAllRemainingTranslations() {
        return remainingTranslationService.getAllRemainingTranslations();
    }

}
