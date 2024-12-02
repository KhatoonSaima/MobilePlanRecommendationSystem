package com.mac.acc.features;

import com.mac.acc.ui.LogEntry;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A class that implements SearchFrequencyQuery interface to track and analyze search queries
 * Uses a self-balancing tree (TreeMap) for frequency tracking and ArrayList for logging
 */
public class SimpleSearchFrequencyQuery implements SearchFrequencyQuery {
    // TreeMap (Red-Black tree) to store search queries (key) and their frequencies (value)
    // Automatically maintains keys in sorted order
    private final TreeMap<String, Integer> searchTree = new TreeMap<>();

    // ArrayList to maintain chronological history of all searches
    private final List<LogEntry> log = new ArrayList<>();

    /**
     * Returns the top N most frequently searched queries
     * @param n The number of top queries to return
     * @return List of entries containing query strings and their frequencies,
     *         sorted by frequency in descending order
     */
    @Override
    public List<AbstractMap.SimpleEntry<String, Integer>> getTopNSearchedQueries(int n) {
        // Handle edge cases
        if (n < 0) return null;                    // Invalid input
        if (n == 0) return new ArrayList<>();      // Empty list requested
        if (n > searchTree.size()) n = searchTree.size();  // Adjust if n exceeds available entries

        // Convert TreeMap entries to stream, sort by values (frequencies) in descending order,
        // take top n entries, create deep copies of entries, and collect to list
        return searchTree.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .map(pair -> new AbstractMap.SimpleEntry<>(pair.getKey(), pair.getValue()))
                .toList();
    }

    /**
     * Returns all searched queries sorted by frequency in descending order
     * @return List of all entries containing query strings and their frequencies
     */
    @Override
    public List<AbstractMap.SimpleEntry<String, Integer>> getTopNSearchedQueries() {
        return getTopNSearchedQueries(searchTree.size());
    }

    /**
     * Records a new search query by updating its frequency and logging the event
     * @param query The search query string to be recorded
     */
    @Override
    public void insertSearchedQuery(String query) {
        // Increment frequency count (or initialize to 1 if first occurrence)
        searchTree.put(query, searchTree.getOrDefault(query, 0) + 1);
        // Add entry to chronological log with current timestamp
        log.add(new LogEntry(query, LocalDateTime.now()));
    }

    /**
     * Returns the most recent n log entries
     * @param n The number of most recent log entries to return
     * @return List of the most recent n LogEntry objects (deep copy)
     */
    @Override
    public List<LogEntry> getLog(int n) {
        // Handle edge cases
        if (n < 0) return null;                // Invalid input
        if (n == 0) return new ArrayList<>();  // Empty list requested
        if (n >= log.size()) return log;       // Return all if n exceeds log size

        // Get sublist of most recent n entries, create deep copies, and collect to list
        return log.subList(log.size() - n, log.size()).stream()
                .map(entry -> new LogEntry(entry.getQuery(), entry.getTimestamp()))
                .toList();
    }

    /**
     * Returns all log entries in chronological order
     * @return List of all LogEntry objects
     */
    @Override
    public List<LogEntry> getLog() {
        return getLog(log.size());
    }
}