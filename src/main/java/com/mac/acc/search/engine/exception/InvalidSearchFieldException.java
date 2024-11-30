package com.mac.acc.search.engine.exception;

/**
 * Exception thrown when a search field is invalid or improperly configured.
 *
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public class InvalidSearchFieldException extends SearchEngineException {
    public InvalidSearchFieldException(String message) {
        super(message);
    }
}
