package com.mac.acc.features;

import com.mac.acc.ui.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;

class SearchBarTest {
    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() throws IOException {
        // Create test CSV file
        File csvFile = new File(tempDir.toFile(), "mobile_plans.csv");
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("Plan Name,Data,Features\n");
            writer.write("Basic Plan,5GB,Unlimited calls\n");
            writer.write("Premium Plan,10GB,International roaming\n");
        }

        // Set system property to point to test file
        System.setProperty("csv.path", csvFile.getAbsolutePath());
    }

    @Test
    void testGetWordCompletions() {
        List<String> completions = SearchBar.getWordCompletions("prem");
        assertNotNull(completions);
        assertTrue(completions.contains("premium"));
    }

    @Test
    void testGetWordCompletionsEmptyInput() {
        List<String> completions = SearchBar.getWordCompletions("");
        assertFalse(completions.isEmpty());
    }

    @Test
    void testFindCorrections() {
        List<String> corrections = SearchBar.findCorrections("premum", 2);
        assertNotNull(corrections);
        assertTrue(corrections.contains("premium"));
    }

    @Test
    void testHandleSearch() {
        String result = SearchBar.handleSearch("plan", 3, 2);
        assertNotNull(result);
        assertTrue(result.contains("plan"));
    }

    @Test
    void testDisplaySearchFrequency() {
        // Perform some searches first
        SearchBar.handleSearch("plan", 3, 2);
        SearchBar.handleSearch("data", 3, 2);
        SearchBar.handleSearch("plan", 3, 2);

        String result = SearchBar.displaySearchFrequency();
        assertNotNull(result);
        assertTrue(result.contains("plan"));
        assertTrue(result.contains("2")); // "plan" should appear twice
    }
}

class SpellCheckerTest {
    @Test
    void testTrieInsertion() {
        SpellChecker.Trie trie = new SpellChecker.Trie();
        trie.insertWord("test");
        assertTrue(trie.searchWord("test"));
    }

    @Test
    void testTrieSearch() {
        SpellChecker.Trie trie = new SpellChecker.Trie();
        trie.insertWord("hello");
        assertTrue(trie.searchWord("hello"));
        assertFalse(trie.searchWord("hell"));
    }

    @Test
    void testComputeEditDistance() {
        int distance = SpellChecker.Trie.computeEditDistance("hello", "helo");
        assertEquals(1, distance);
    }

    @Test
    void testSuggestSimilarWords() {
        SpellChecker.Trie trie = new SpellChecker.Trie();
        trie.insertWord("hello");
        trie.insertWord("helo");
        trie.insertWord("help");

        List<String> suggestions = SpellChecker.Trie.suggestSimilarWords(trie, "helo", 2);
        assertNotNull(suggestions);
        assertFalse(suggestions.contains("hello"));
    }

    @Test
    void testCaseInsensitiveSearch() {
        SpellChecker.Trie trie = new SpellChecker.Trie();
        trie.insertWord("Hello");
        assertFalse(trie.searchWord("hello"));
    }
}

class WordCompletionTest {
    @Test
    void testInsert() {
        WordCompletion trie = new WordCompletion();
        trie.insert("test");
        List<String> completions = trie.prefixfinder("te");
        assertNotNull(completions);
        assertTrue(completions.contains("test"));
    }

    @Test
    void testPrefixFinder() {
        WordCompletion trie = new WordCompletion();
        trie.insert("test");
        trie.insert("testing");
        trie.insert("tester");

        List<String> completions = trie.prefixfinder("test");
        assertNotNull(completions);
        assertEquals(3, completions.size());
        assertTrue(completions.contains("test"));
        assertTrue(completions.contains("testing"));
        assertTrue(completions.contains("tester"));
    }

    @Test
    void testPrefixFinderWithInvalidInput() {
        WordCompletion trie = new WordCompletion();
        trie.insert("test");

        List<String> completions = trie.prefixfinder("test123");
        assertTrue(completions.isEmpty());
    }

    @Test
    void testPrefixFinderWithEmptyInput() {
        WordCompletion trie = new WordCompletion();
        trie.insert("test");

        List<String> completions = trie.prefixfinder("");
        assertFalse(completions.isEmpty());
    }

    @Test
    void testPrefixFinderWithNoMatches() {
        WordCompletion trie = new WordCompletion();
        trie.insert("test");

        List<String> completions = trie.prefixfinder("xyz");
        assertTrue(completions.isEmpty());
    }
}

class SearchFrequencyQueryTest {
    @Test
    void testSimpleSearchFrequencyQuery() {
        SearchFrequencyQuery query = new SimpleSearchFrequencyQuery();

        query.insertSearchedQuery("test");
        query.insertSearchedQuery("test");
        query.insertSearchedQuery("other");

        List<AbstractMap.SimpleEntry<String, Integer>> results = query.getTopNSearchedQueries(2);
        assertEquals(2, results.size());
        assertEquals("test", results.get(0).getKey());
        assertEquals(2, results.get(0).getValue());
    }

    @Test
    void testGetLog() {
        SearchFrequencyQuery query = new SimpleSearchFrequencyQuery();
        query.insertSearchedQuery("test");

        List<LogEntry> log = query.getLog();
        assertFalse(log.isEmpty());
        assertEquals("test", log.get(0).getQuery());
    }
}