package com.rszumlas.dictionary.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Translation")
@Table(name = "translation")
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id",
            updatable = false
    )
    private Long translation_id;

    @Column(
            name = "polish_word",
            columnDefinition = "TEXT",
            unique = true
    )
    private String polishWord;

    @Column(
            name = "english_word",
            columnDefinition = "TEXT",
            unique = true
    )
    private String englishWord;
}
