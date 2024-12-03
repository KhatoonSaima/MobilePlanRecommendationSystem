package com.mac.acc.features;

/**
 * @author weiming zheng
 * @since 2024-10-29
 */
public class SearchFrequencyQueryFactory {
    public static SearchFrequencyQuery getSearchFrequencyQuery(String code) {
        return new SimpleSearchFrequencyQuery();
    }
}