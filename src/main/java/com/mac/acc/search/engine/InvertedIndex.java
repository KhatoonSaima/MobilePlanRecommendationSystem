package com.mac.acc.search.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Implementation of a Trie-based inverted index for efficient word lookup.
 * Stores word frequency counts for each document in the collection.
 * @author Weiming Zheng (Main contributor) with Saima Khatoon (contributed to line of code for Frequency Counts from 58 to 60)
 * @since 2024-11-30
 */
@Slf4j
public class InvertedIndex implements Trie {
    /** Current node index for trie construction */
    private int idx = 0;

    /** List of maps representing trie nodes and their children */
    private final List<Map<Character, Integer>> ne = new ArrayList<>();

    /** Total number of documents in the collection */
    private final int n;

    /** Frequency counts for words at each trie node */
    private final List<List<Integer>> freqCnts = new ArrayList<>();

    /**
     * Creates a new inverted index for the specified number of documents.
     * @param n Number of documents in the collection
     */
    public InvertedIndex(int n) {
        this.n = n;
        this.ne.add(new HashMap<>());
        this.freqCnts.add(new ArrayList<>(Collections.nCopies(n, 0)));
        this.freqCnts.add(null);
        log.info("Initialized InvertedIndex for {} documents", n);
    }

    @Override
    public void insert(String word, int documentNo) {
        log.debug("Inserting word '{}' for document {}", word, documentNo);
        int u = 0;
        for (Character c : word.toCharArray()) {
            if (!ne.get(u).containsKey(c)) {
                ++idx;
                ne.get(u).put(c, idx);
                ne.add(new HashMap<>());
                freqCnts.add(null);
            }
            u = ne.get(u).get(c);
        }

        // Initialize frequency count vector if needed
        if (freqCnts.get(u) == null) {
            freqCnts.set(u, new ArrayList<>(Collections.nCopies(n, 0)));
        }

        // Increment frequency count for this word in the document (credit to Saima Khatoon's Frequency Count implementation)
        List<Integer> counts = freqCnts.get(u);
        counts.set(documentNo, counts.get(documentNo) + 1);
    }

    @Override
    public List<Integer> query(String word) {
        log.debug("Querying word: '{}'", word);
        int u = 0;
        for (Character c : word.toCharArray()) {
            if (!ne.get(u).containsKey(c)) {
                log.debug("Word '{}' not found in index", word);
                return new ArrayList<>(Collections.nCopies(n, 0));
            }
            u = ne.get(u).get(c);
        }

        log.debug("Found frequency vector for word '{}'", word);
        if (freqCnts.get(u) == null) return new ArrayList<>(Collections.nCopies(n, 0));
        return freqCnts.get(u);
    }
}