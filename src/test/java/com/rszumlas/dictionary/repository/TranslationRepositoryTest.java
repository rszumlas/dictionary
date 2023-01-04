package com.rszumlas.dictionary.repository;

import com.rszumlas.dictionary.model.Translation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TranslationRepositoryTest {

    @Autowired
    private TranslationRepository underTest;
    private static final Logger LOGGER = LoggerFactory.getLogger(TranslationRepositoryTest.class);

    @Test
    void itShouldFindTranslationByWord() {
        // Given
        String word = "kot";
        Translation translation = new Translation(1L, word, "cat");

        underTest.saveAndFlush(translation);
        LOGGER.info(String.format("Saved translation - %s", translation));

        // When
        Optional<Translation> foundTranslation = underTest.findTranslationByWord(word);
        LOGGER.info(String.format("Retrieved translation through findTranslationByWord - %s", foundTranslation.toString()));

        // Then
        assertThat(foundTranslation)
                .isPresent()
                .hasValueSatisfying(t -> {
                    assertThat(t).usingRecursiveComparison()
                            .isEqualTo(translation);
                });
    }


}
