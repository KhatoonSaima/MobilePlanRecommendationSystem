package com.mac.acc.search;

import com.mac.acc.search.engine.SearchEngine;
import com.mac.acc.search.engine.SearchEngineFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Weiming Zheng
 * @since 2024-11-30
 */
public class SearchEngineSingleton {
    private static SearchEngine instance;
    private static List<Document> documents;

    private SearchEngineSingleton() {}

    public static synchronized SearchEngine getInstance() {
        if (instance == null) {
            documents = loadDocuments();
            instance = SearchEngineFactory.getSearchEngine(documents);
        }
        return instance;
    }

    private static List<Document> loadDocuments() {
        List<Document> testDocuments = new ArrayList<>();

        try (InputStream is = SearchEngineSingleton.class.getClassLoader().getResourceAsStream("URLs.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String url;
            int index = 1;
            while ((url = reader.readLine()) != null) {
                if (!url.trim().isEmpty()) {
                    testDocuments.add(new Document(
                            Path.of("docs/" + index + ".html"),
                            url.trim()
                    ));
                    index++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading URLs: " + e.getMessage());
        }

        return testDocuments;
    }
}