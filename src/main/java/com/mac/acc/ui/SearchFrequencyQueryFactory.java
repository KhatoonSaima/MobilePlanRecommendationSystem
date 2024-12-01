package com.mac.acc.ui;

public class SearchFrequencyQueryFactory {
    public static SearchFrequencyQuery getSearchFrequencyQuery(String code) {
        return new SimpleSearchFrequencyQuery();
    }
}
