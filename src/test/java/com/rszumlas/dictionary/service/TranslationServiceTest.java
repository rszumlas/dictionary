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

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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

    // getSentenceTranslation
    @Test
    void itShouldGetSentenceTranslation() {
        // Given
        String sentence = "Lubię jeździć rowerem";

        given(translationRepository.findTranslationByWord("Lubię"))
                .willReturn(Optional.of(new Translation(1L, "lubię", "I like")));
        given(translationRepository.findTranslationByWord("jeździć"))
                .willReturn(Optional.of(new Translation(2L, "jeździć", "riding")));
        given(translationRepository.findTranslationByWord("rowerem"))
                .willReturn(Optional.of(new Translation(3L, "rowerem", "a bike")));

        // When
        String translatedSentence = underTest.getSentenceTranslation(sentence);

        // Then
        assertEquals("I like riding a bike", translatedSentence);
    }

    @Test
    void itShouldNotTranslateGivenWordInSentence() {
        // Given
        List<String> wordsList = List.of("Lubię", "jeździć", "rowerem");
        String word = wordsList.get(0);

        // When
        underTest.tryCatchGetTranslation(wordsList, word);

        // Then
        assertEquals(wordsList.get(0), word);
    }
    @Test
    void itShouldTranslateGivenWordInSentence() {
        // Given
        String[] words = {"Lubię", "jeździć", "rowerem"};
        List<String> wordsList = new ArrayList<>(Arrays.asList(words));
        String word = wordsList.get(0);
        Optional<Translation> translation = Optional.of(new Translation(1L, "Lubię", "I like"));

        given(translationRepository.findTranslationByWord(word)).willReturn(translation);

        // When
        underTest.tryCatchGetTranslation(wordsList, word);

        // Then
        assertEquals(translation.get().getEnglishWord(), wordsList.get(0));
    }

    @Test
    void itShouldCastSentenceToListOfWords() {
        // Given
        String sentence = "Lubię jeździć rowerem";
        List<String> wordsList = List.of("Lubię", "jeździć", "rowerem");

        // When
        List<String> castedSentence = underTest.castSentenceToListOfWords(sentence);

        // Then
        assertEquals(wordsList, castedSentence);
    }

    // getWordTranslation
    @Test
    void itShouldGetWordTranslation() {
        // Given
        String polishWord = "dom";
        Translation translation = new Translation(1L, polishWord, "house");
        given(translationRepository.findTranslationByWord(polishWord))
                .willReturn(Optional.of(translation));

        // When
        String translatedWord = underTest.getWordTranslation(polishWord);

        // Then
        assertThat(translatedWord)
                .isEqualTo(translation.getEnglishWord());
    }

    @Test
    void itShouldThrowWhenWordTranslationNotFound() {
        // Given
        String polishWord = "dom";
        given(translationRepository.findTranslationByWord(polishWord))
                .willReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getWordTranslation(polishWord))
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