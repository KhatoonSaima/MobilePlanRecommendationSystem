package com.mac.acc.search.engine;

import lombok.Getter;

/**
 * Enumeration of possible field conditions for search queries.
 * Determines how words within a field are matched across documents.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
@Getter
public enum FieldCondition {
    /** All words in the field must appear in the document */
    TIGHT("tight"),

    /** Documents containing any words in the field are considered matches */
    LOOSE("loose");

    /** String representation of the condition */
    private final String value;

    FieldCondition(String value) {
        this.value = value;
    }
}