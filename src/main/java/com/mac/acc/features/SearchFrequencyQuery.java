package com.mac.acc.features;

import com.mac.acc.ui.LogEntry;

import java.util.AbstractMap;
import java.util.List;

/**
 * Interface for tracking and managing the frequency of search queries.
 * Implementations of this interface should store search queries in a
 * self-balancing search tree structure (i.e. red-black tree)
 * to ensure efficient retrieval of query frequencies.
 *
 * It provides methods to:
 * <ul>
 *   <li>Insert search queries and update their frequencies</li>
 *   <li>Retrieve the most frequently searched queries</li>
 *   <li>Log searches with timestamps for history tracking</li>
 * </ul>
 *
 * @author Weiming Zheng
 * @since 2024-10-29
 */
public interface SearchFrequencyQuery {

    /**
     * Retrieves the top N most frequently searched queries, in descending order
     * of frequency. If multiple queries have the same frequency, the order is
     * implementation-dependent.
     *
     * @param n the maximum number of top queries to retrieve. If n is less than
     *          or equal to zero, an empty list or null will be returned depending
     *          on the implementation.
     * @return a list of up to N entries representing the most frequently searched
     *         queries, where each entry contains a query string and its frequency.
     *         Returns an empty list if n is zero or if there are no stored queries,
     *         and may return null if n is negative.
     */
    List<AbstractMap.SimpleEntry<String, Integer>> getTopNSearchedQueries(int n);

    /**
     * Retrieves all stored queries sorted in descending order of frequency.
     *
     * @return a list of all entries representing stored queries, where each entry
     *         contains a query string and its frequency. The list is ordered by
     *         frequency in descending order.
     */
    List<AbstractMap.SimpleEntry<String, Integer>> getTopNSearchedQueries();

    /**
     * Inserts a search query into the frequency tracking system.
     * If the query already exists, its frequency is incremented by 1.
     * Additionally, logs the query with a timestamp for future reference.
     *
     * @param query the search query to insert and track. Must be a non-null string.
     */
    void insertSearchedQuery(String query);

    /**
     * Retrieves the most recent N log entries, where each entry includes a
     * search query and the timestamp when it was logged. Log entries are
     * returned in chronological order (oldest to newest).
     *
     * @param n the maximum number of recent log entries to retrieve. If n is less than
     *          or equal to zero, an empty list or null will be returned depending on
     *          the implementation.
     * @return a list of up to N log entries, each entry containing a query string and
     *         a timestamp. Returns an empty list if n is zero or if there are no log
     *         entries, and may return null if n is negative.
     */
    List<LogEntry> getLog(int n);

    /**
     * Retrieves the entire log history of search queries in chronological order.
     *
     * @return a list of all log entries, where each entry contains a query string
     *         and the timestamp when it was logged. The list is ordered by
     *         timestamp from oldest to newest.
     */
    List<LogEntry> getLog();
}
