package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.Translation;
import com.rszumlas.dictionary.repository.TranslationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TranslationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TranslationRepository translationRepository;

    @Test
    void itShouldGetSentenceTranslation() throws Exception {
        // Given
        String sentence = "Lubię jeździć rowerem";

        // When
        // Then
        mockMvc.perform(get("/api/v1/translation/sentence/{sentence}", sentence))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldGetWordTranslation() throws Exception {
        // Given
        String word = "kot";
        Optional<Translation> translation = Optional.of(new Translation(1L, word, "cat"));
        given(translationRepository.findTranslationByWord(word)).willReturn(translation);

        // When
        // Then
        mockMvc.perform(get("/api/v1/translation/word/{word}", word))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldGetTranslationPage() throws Exception {
        // Given
        PageRequest pageRequest = PageRequest.of(1, 10);
        given(translationRepository.findAll(pageRequest)).willReturn(mock(Page.class));

        // When
        // Then
        mockMvc.perform(get("/api/v1/translation/{page}/{size}", pageRequest.getPageNumber(), pageRequest.getPageSize()))
                .andExpect(status().isOk());
    }

    @Test
    void itShouldSaveTranslation() throws Exception {
        // Given
        String polishWord = "kot";
        String englishWord = "cat";

        // When
        // Then
        mockMvc.perform(post("/api/v1/translation/{polish-word}/{english-word}", polishWord, englishWord))
                .andExpect(status().isOk());
    }

}