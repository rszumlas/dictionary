package com.rszumlas.dictionary.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RemainingTranslation")
@Table(name = "remaining_translation")
public class RemainingTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id",
            updatable = false
    )
    private Long remaining_translation_id;

    @Column(
            name = "word",
            columnDefinition = "TEXT",
            unique = true
    )
    private String word;
}
