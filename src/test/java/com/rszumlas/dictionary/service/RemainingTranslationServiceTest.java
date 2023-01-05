package com.rszumlas.dictionary.service;

import com.rszumlas.dictionary.model.RemainingTranslation;
import com.rszumlas.dictionary.repository.RemainingTranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class RemainingTranslationServiceTest {

    @Mock
    private RemainingTranslationRepository remainingTranslationRepository;
    private RemainingTranslationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new RemainingTranslationService(remainingTranslationRepository);
    }

    @Test
    void itShouldGetAllRemainingTranslations() {
        // Given
        List<RemainingTranslation> mockList = new ArrayList<>();
        given(remainingTranslationRepository.findAll())
                .willReturn(mockList);

        // When
        List<RemainingTranslation> remainingTranslationList = underTest.getAllRemainingTranslations();

        // Then
        assertEquals(mockList, remainingTranslationList);
    }
}