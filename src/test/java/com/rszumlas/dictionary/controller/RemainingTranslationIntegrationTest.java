package com.rszumlas.dictionary.controller;

import com.rszumlas.dictionary.model.RemainingTranslation;
import com.rszumlas.dictionary.repository.RemainingTranslationRepository;
import com.rszumlas.dictionary.repository.TranslationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RemainingTranslationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RemainingTranslationRepository remainingTranslationRepository;

    @Test
    void itShouldGetAllRemainingTranslations() throws Exception {
        // Given
        List<RemainingTranslation> remainingTranslationList = List.of(
                new RemainingTranslation(1L, "cat"),
                new RemainingTranslation(2L, "dom"),
                new RemainingTranslation(3L, "dog")
        );
        given(remainingTranslationRepository.findAll()).willReturn(remainingTranslationList);

        // When
        // Then
        mockMvc.perform(get("/api/v1/remaining-translation"))
                .andExpect(status().isOk());
    }
}