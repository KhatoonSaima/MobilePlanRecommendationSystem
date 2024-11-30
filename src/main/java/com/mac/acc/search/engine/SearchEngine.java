package com.mac.acc.search.engine;

import com.mac.acc.search.Field;

import java.util.List;
import java.util.Map.Entry;

/**
 * Interface defining the core functionality of the search engine.
 * Implementations should provide document searching capabilities based on field criteria.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public interface SearchEngine {
    /**
     * Searches the document collection based on the provided field criteria.
     *
     * @param fields List of search fields with their conditions
     * @return List of document URLs and their relevance scores, sorted by relevance
     */
    List<Entry<String, Integer>> search(List<Field> fields);
}
