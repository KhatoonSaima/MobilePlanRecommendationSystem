package com.mac.acc.search.engine.exception;

/**
 * Base exception class for all search engine related exceptions.
 * Provides a hierarchy for different types of search-related errors.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public class SearchEngineException extends RuntimeException {
    public SearchEngineException(String message) {
        super(message);
    }

    public SearchEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
