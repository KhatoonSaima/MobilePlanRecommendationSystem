package com.mac.acc.features;

public class SearchFrequencyQueryFactory {
    public static SearchFrequencyQuery getSearchFrequencyQuery(String code) {
        return new SimpleSearchFrequencyQuery();
    }
}