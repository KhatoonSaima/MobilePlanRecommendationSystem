package com.mac.acc.search;

import com.mac.acc.search.engine.FieldCondition;
import com.mac.acc.search.engine.exception.InvalidSearchFieldException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a search field with its condition and case sensitivity settings.
 * Used to define search criteria in the mobile data plan search engine.
 *
 * @author Weiming Zheng
 * @since 2024-11-30
 */
@Data
@Slf4j
public class Field {
    private String value;
    private FieldCondition scope;
    private boolean isCaseSensitive;

    public Field(String value, FieldCondition scope, boolean isCaseSensitive) {
        this.value = (value == null || value.isEmpty()) ? "" : value;
        this.scope = value == null ? FieldCondition.LOOSE : scope;
        this.isCaseSensitive = isCaseSensitive;
    }
}
