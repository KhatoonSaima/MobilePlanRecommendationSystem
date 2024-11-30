package com.mac.acc.search.engine;

import java.util.List;

/**
 * Interface for trie-based data structure implementation.
 * Used for efficient word storage and retrieval in the search engine.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public interface Trie {
    /**
     * Inserts a word into the trie for a specific document.
     *
     * @param word Word to insert
     * @param documentNo Document identifier
     */
    void insert(String word, int documentNo);

    /**
     * Queries the trie for a word and returns its document frequency vector.
     *
     * @param word Word to query
     * @return List of frequency counts for the word across all documents
     */
    List<Integer> query(String word);
}