package com.rszumlas.dictionary.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryReport {

    private Long wordCount;
    private Map<Long, Long> polishWordsLengthCount;
    private Map<Long, Long> englishWordsLengthCount;
    private Double avgPolishWordLength;
    private Double avgEnglishWordLength;

}
