/**
 * @author Tausif Zaman
 * @since 2024-11-30
 */

package com.mac.acc.features;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class SearchBar {

    private static final SearchFrequencyQuery frequencyTracker = SearchFrequencyQueryFactory.getSearchFrequencyQuery("simple");

    // Word Completion Using Trie
    public static List<String> getWordCompletions(String input) {
        WordCompletion trie = new WordCompletion(); // Assume WordCompletion is your Trie class for completions

        String csvFile = "src/main/resources/mobile_plans.csv"; // Replace with your actual file path
        String line;
        String delimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] cells = line.split(delimiter);
                for (String cell : cells) {
                    String cleanedCell = cell.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
                    String[] words = cleanedCell.trim().split("\\s+");
                    for (String word : words) {
                        trie.insert(word); // Insert each word into the Trie
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return completions for the current prefix input
        return trie.prefixfinder(input);
    }

    // Spell Checker using Tries
    public static List<String> findCorrections(String wordInput, int maxEditDistance) {
        List<String> corrections = new ArrayList<>();
        SpellChecker.Trie dictionary = new SpellChecker.Trie(); // Assume SpellChecker.Trie is your Trie class for corrections

        String filename = "src/main/resources/mobile_plans.csv"; // Replace with your actual file path

        try {
            Scanner csvScan = new Scanner(new File(filename));
            while (csvScan.hasNextLine()) {
                String currLine = csvScan.nextLine();
                String[] wordsInString = currLine.split("[^\\w.]+|(?<!\\w)\\.|\\.(?!\\w)", 0);
                for (String word : wordsInString) {
                    if (!word.trim().isEmpty()) {
                        dictionary.insertWord(word.toLowerCase());
                    }
                }
            }
            csvScan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }

        // Check if the word exists
        if (dictionary.searchWord(wordInput.toLowerCase())) {
            return corrections; // No corrections needed if the word exists
        } else {
            return dictionary.suggestSimilarWords(dictionary, wordInput.toLowerCase(), maxEditDistance);
        }
    }

    // Method to handle search logic and update frequency
    public static String handleSearch(String input, int maxSuggestions, int maxEditDistance) {
        // Step 1: Exact match check
        List<String> completions = getWordCompletions(input);
        if (completions.contains(input)) { // Check for an exact match in the completions
            frequencyTracker.insertSearchedQuery(input); // Track the search query
            return "Word match found: " + input;
        }

        // Step 2: Show word completions if no exact match
        if (!completions.isEmpty()) {
            frequencyTracker.insertSearchedQuery(input); // Track the search query
            return "Word Completions: " + completions.subList(0, Math.min(maxSuggestions, completions.size()));
        }

        // Step 3: Check for spelling corrections
        List<String> corrections = findCorrections(input, maxEditDistance);
        if (!corrections.isEmpty()) {
            frequencyTracker.insertSearchedQuery(input); // Track the search query
            return "Did you mean: " + corrections + "?";
        }

        // Step 4: If no match or corrections found
        frequencyTracker.insertSearchedQuery(input); // Track the search query
        return "No match found.";
    }

    // Method to display the most frequently searched queries
    public static String displaySearchFrequency() {
        List<AbstractMap.SimpleEntry<String, Integer>> topQueries = frequencyTracker.getTopNSearchedQueries(10); // Top 10 queries
        if (topQueries.isEmpty()) {
            return "No searches yet.";
        }

        StringBuilder result = new StringBuilder("Search Frequency:\n");
        int rank = 1;
        for (AbstractMap.SimpleEntry<String, Integer> entry : topQueries) {
            result.append(rank++).append(". ").append(entry.getKey())
                    .append(" : Searched ").append(entry.getValue()).append(" times\n");
        }

        return result.toString();
    }

    // Testing the search and frequency tracking
    public static void main(String[] args) {
        // Simulate some searches
        System.out.println(handleSearch("apple", 3, 2)); // Example search
        System.out.println(handleSearch("app", 3, 2));   // Example search
        System.out.println(handleSearch("appl", 3, 2));  // Example search

        // Display the search frequency
        System.out.println(displaySearchFrequency());
    }
}
