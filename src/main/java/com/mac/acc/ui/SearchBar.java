package com.mac.acc.ui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.*;

public class SearchBar {

    // Word Completion Using Trie
    public static List<String> getWordCompletions(String input) {
        WordCompletion trie = new WordCompletion();

        String csvFile = "fido.csv";
        String line;
        String delimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] cells = line.split(delimiter);
                for (String cell : cells) {
                    String cleanedCell = cell.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
                    String[] words = cleanedCell.trim().split("\\s+");
                    for (String word : words) {
                        trie.insert(word);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trie.prefixfinder(input);
    }

    // Spell Checker using Tries
    public static List<String> findCorrections(String wordInput, int maxEditDistance) {
        List<String> corrections = new ArrayList<>();
        SpellChecker.Trie dictionary = new SpellChecker.Trie();

        String filename = "phoneplans.csv";

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

        if (dictionary.searchWord(wordInput.toLowerCase())) {
            return corrections; // No corrections needed if the word exists
        } else {
            return dictionary.suggestSimilarWords(dictionary, wordInput.toLowerCase(), maxEditDistance);
        }
    }
}
