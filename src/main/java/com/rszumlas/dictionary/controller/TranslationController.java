package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/translation")
@RestController
public class TranslationController {

    private final TranslationService translationService;

}
