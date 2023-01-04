package com.rszumlas.dictionary.repository;

import com.rszumlas.dictionary.model.RemainingTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RemainingTranslationRepository extends JpaRepository<RemainingTranslation, Long> {
}
