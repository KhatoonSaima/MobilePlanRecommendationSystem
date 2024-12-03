package com.mac.acc.search.engine;

import com.mac.acc.search.Document;

import java.util.List;

/**
 * Factory class for creating search engine instances.
 * Currently, provides a MobileDataPlanSearchEngine implementation specialized for
 * searching and analyzing mobile data plan documents.
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public class SearchEngineFactory {
    /**
     * Creates and returns a new search engine instance.
     *
     * @param documents List of documents to be indexed and searched
     * @return A configured SearchEngine instance
     */
    public static SearchEngine getSearchEngine(List<Document> documents) {
        return new MobileDataPlanSearchEngine(documents);
    }
}