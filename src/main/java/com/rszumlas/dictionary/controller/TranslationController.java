package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.DictionaryReport;
import com.rszumlas.dictionary.model.Translation;
import com.rszumlas.dictionary.service.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/v1/translations/")
@RestController
public class TranslationController {

    private final TranslationService translationService;

    @Operation(
            summary = "Get sentence translation",
            description = "Words that could not be translated remain in their original form. A word unknown to the dictionary is saved in the database."
    )
    @GetMapping(path = "sentence/{sentence}")
    public String getSentenceTranslation(@PathVariable("sentence") String sentence) {
        return translationService.getSentenceTranslation(sentence);
    }

    @Operation(
            summary = "Get word translation",
            description = "Takes a single word and returns its translation."
    )
    @GetMapping(path = "word/{word}")
    public String getWordTranslation(@PathVariable("word") String word) {
        return translationService.getWordTranslation(word);
    }

    @Operation(
            summary = "Get pdf dictionary report",
            description = "Returns a pdf report on the state of the dictionary, containing the following information:\n" +
                    "    number of words in the dictionary,\n" +
                    "    number of words of particular length (separately for each language),\n" +
                    "    average length of all words (separately for each language),\n" +
                    "    number of words to be completed."
    )
    @GetMapping(path = "pdf-report")
    public ResponseEntity<InputStreamResource> getPdfReport() {

        ByteArrayInputStream pdf = translationService.getPdfReport();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline;file=report.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @Operation(
            summary = "Get dictionary report",
            description = "Returns a report on the state of the dictionary, containing the following information:\n" +
                    "    number of words in the dictionary,\n" +
                    "    number of words of particular length (separately for each language),\n" +
                    "    average length of all words (separately for each language),\n" +
                    "    number of words to be completed."
    )
    @GetMapping(path = "report")
    public DictionaryReport getDictionaryReport() {
        return translationService.getDictionaryReport();
    }

    @Operation(
            summary = "Return a list of words in the dictionary",
            description = "Returns a list of words available in the dictionary. The list is returned with pagination, for which the number of elements on the page and the page number, are sent in the form of request parameters."
    )
    @GetMapping(path = "{page}/{size}")
    public List<Translation> getTranslationPage(@PathVariable("page") Integer page,
                                                @PathVariable("size") Integer size) {
        return translationService.getTranslationPage(page, size);
    }

    @Operation(
            summary = "Add a word to the dictionary",
            description = "Takes the word in Polish and English and saves it to the dictionary database"
    )
    @PostMapping(path = "{polish-word}/{english-word}")
    public void saveTranslation(@PathVariable("polish-word") String polishWord,
                                @PathVariable("english-word") String englishWord) {
        translationService.saveTranslation(polishWord, englishWord);
    }

}
