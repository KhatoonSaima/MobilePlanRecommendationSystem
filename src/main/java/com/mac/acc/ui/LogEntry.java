package com.mac.acc.ui;
import java.time.LocalDateTime;

/**
 * Represents a single log entry that captures a query and its timestamp.
 * This class is designed to store and manage individual logging events,
 * typically used for tracking and auditing purposes.
 *
 * @author Weiming Zheng
 * @since 2024-10-29
 */
public class LogEntry {
    // Stores the query string associated with this log entry
    private String query;

    // Stores the timestamp when this log entry was created
    private LocalDateTime timestamp;

    /**
     * Constructs a new LogEntry with the specified query and timestamp.
     *
     * @param query The query string to be logged
     * @param timestamp The date and time when the query was executed
     */
    public LogEntry(String query, LocalDateTime timestamp) {
        this.query = query;
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the query string associated with this log entry.
     *
     * @return The query string stored in this log entry
     */
    public String getQuery() {
        return query;
    }

    /**
     * Retrieves the timestamp when this log entry was created.
     *
     * @return The LocalDateTime object representing when the query was executed
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}