package com.rszumlas.dictionary.repository;

import com.rszumlas.dictionary.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TranslationRepository extends JpaRepository<Translation, Long> {

    @Query(
            value = "SELECT * FROM translation WHERE :word in (polish, english)",
            nativeQuery = true
    )
    Optional<Translation> findTranslationByWord(@Param("word") String word);

}
