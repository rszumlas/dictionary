package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.Translation;
import com.rszumlas.dictionary.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/translation")
@RestController
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping(path = "/word/{word}")
    public String getTranslation(@PathVariable("word") String word) {
        return translationService.getTranslation(word);
    }

    @GetMapping(path = "{page}/{size}")
    public List<Translation> getTranslationPage(@PathVariable("page") Integer page,
                                                @PathVariable("size") Integer size) {
        return translationService.getTranslationPage(page, size);
    }

    @PostMapping(path = "{polish-word}/{english-word}")
    public void saveTranslation(@PathVariable("polish-word") String polishWord,
                                @PathVariable("english-word") String englishWord) {
        translationService.saveTranslation(polishWord, englishWord);
    }

    @GetMapping(path = "/sentence/{sentence}")
    public String getSentenceTranslation(@PathVariable("sentence") String sentence) {
        return translationService.getSentenceTranslation(sentence);
    }

}