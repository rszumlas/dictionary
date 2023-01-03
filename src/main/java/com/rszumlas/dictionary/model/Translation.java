package com.rszumlas.dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
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
            name = "polish",
            columnDefinition = "TEXT",
            unique = true
    )
    private String polish;

    @Column(
            name = "english",
            columnDefinition = "TEXT",
            unique = true
    )
    private String english;
}
