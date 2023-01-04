package com.rszumlas.dictionary.service;

import com.rszumlas.dictionary.exception.ApiRequestException;
import com.rszumlas.dictionary.model.Translation;
import com.rszumlas.dictionary.repository.RemainingTranslationRepository;
import com.rszumlas.dictionary.repository.TranslationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;
    @Mock
    private RemainingTranslationRepository remainingTranslationRepository;
    @Captor
    private ArgumentCaptor<Translation> translationArgumentCaptor;
    private TranslationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new TranslationService(translationRepository, remainingTranslationRepository);
    }

    // getTranslation
    @Test
    void itShouldGetTranslation() {
        // Given
        String polishWord = "dom";
        Translation translation = new Translation(1L, polishWord, "house");
        given(translationRepository.findTranslationByWord(polishWord))
                .willReturn(Optional.of(translation));

        // When
        String translatedWord = underTest.getTranslation(polishWord);

        // Then
        assertThat(translatedWord)
                .isEqualTo(translation.getEnglishWord());
    }

    @Test
    void itShouldThrowWhenTranslationNotFound() {
        // Given
        String polishWord = "dom";
        given(translationRepository.findTranslationByWord(polishWord))
                .willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getTranslation(polishWord))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Translation not found");
    }

    @Test
    void itShouldGetEnglishTranslatedWord() {
        // Given
        String polishWord = "dom";
        String englishWord = "house";
        Translation translation = new Translation(1L, polishWord, englishWord);

        // When
        String translatedWord = underTest.getTranslatedWord(polishWord, translation);

        // Then
        assertEquals(englishWord, translatedWord);
    }

    @Test
    void itShouldGetPolishTranslatedWord() {
        // Given
        String polishWord = "dom";
        String englishWord = "house";
        Translation translation = new Translation(1L, polishWord, englishWord);

        // When
        String translatedWord = underTest.getTranslatedWord(englishWord, translation);

        // Then
        assertEquals(polishWord, translatedWord);
    }

    // getTranslationPage
    @Test
    void itShouldGetTranslationPage() {
        // Given
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<Translation> translationPage = mock(Page.class);
        given(translationRepository.findAll(pageRequest))
                .willReturn(translationPage);

        // When
        List<Translation> foundTranslationPage = underTest.getTranslationPage(pageRequest.getPageNumber(), pageRequest.getPageSize());

        // Then
        assertEquals(translationPage.getContent(), foundTranslationPage);
    }

    // saveTranslation
    @Test
    void itShouldSaveTranslation() {
        // Given
        String polishWord = "pies";
        String englishWord = "dog";

        // When
        underTest.saveTranslation(polishWord, englishWord);

        // Then
        then(translationRepository).should().saveAndFlush(translationArgumentCaptor.capture());
        Translation translationArgumentCaptorValue = translationArgumentCaptor.getValue();
        assertThat(translationArgumentCaptorValue)
                .hasFieldOrPropertyWithValue("polishWord", polishWord)
                .hasFieldOrPropertyWithValue("englishWord", englishWord);
    }
}