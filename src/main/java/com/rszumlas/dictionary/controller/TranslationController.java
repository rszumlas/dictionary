package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.DictionaryReport;
import com.rszumlas.dictionary.model.Translation;
import com.rszumlas.dictionary.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/translation/")
@RestController
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping(path = "sentence/{sentence}")
    public String getSentenceTranslation(@PathVariable("sentence") String sentence) {
        return translationService.getSentenceTranslation(sentence);
    }

    @GetMapping(path = "word/{word}")
    public String getWordTranslation(@PathVariable("word") String word) {
        return translationService.getWordTranslation(word);
    }

    @GetMapping(path = "create-pdf")
    public ResponseEntity<InputStreamResource> createPdf() {

        ByteArrayInputStream pdf = translationService.createPdf();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline;file=report.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping(path = "report")
    public DictionaryReport getDictionaryReport() {
        return translationService.getDictionaryReport();
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

}
