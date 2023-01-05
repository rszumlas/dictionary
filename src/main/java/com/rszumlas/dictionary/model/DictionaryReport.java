package com.rszumlas.dictionary.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryReport {

    private Long wordCount;
    private Map<Long, Long> polishWordsLengthCount;
    private Map<Long, Long> englishWordsLengthCount;
    private Double avgPolishWordLength;
    private Double avgEnglishWordLength;

    @Override
    public String toString() {
        return "Word count: " + wordCount +
                "\n Number of polish words of a particular length (length, quantity): " + polishWordsLengthCount +
                "\n Number of english words of a particular length (length, quantity): " + englishWordsLengthCount +
                "\n Average polish word length: " + avgPolishWordLength +
                "\n Average english word length: " + avgEnglishWordLength;
    }

}
