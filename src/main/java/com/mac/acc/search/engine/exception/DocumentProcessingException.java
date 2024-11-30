package com.mac.acc.search.engine.exception;

/**
 * Exception thrown when errors occur during document processing,
 * such as file reading or parsing failures.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public class DocumentProcessingException extends SearchEngineException {
    public DocumentProcessingException(String message) {
        super(message);
    }

    public DocumentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}