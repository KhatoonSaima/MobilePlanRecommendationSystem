package com.mac.acc.ui;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WordCompletionUI {

    public static JPanel createPanel() {
        JPanel wordCompletionPanel = new JPanel(new FlowLayout());
        JTextField wordCompletionField = new JTextField(30);
        JButton wordCompletionButton = new JButton("Complete Word");

        wordCompletionPanel.add(new JLabel("Word Completion:"));
        wordCompletionPanel.add(wordCompletionField);
        wordCompletionPanel.add(wordCompletionButton);

        JTextArea suggestionsArea = new JTextArea(5, 20);
        suggestionsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(suggestionsArea);
        wordCompletionPanel.add(scrollPane);

        wordCompletionButton.addActionListener(e -> {
            String input = wordCompletionField.getText().trim();
            if (input.isEmpty()) {
                suggestionsArea.setText("Please enter a word to complete!");
            } else {
                List<String> suggestions = getWordCompletions(input);
                if (suggestions.isEmpty()) {
                    suggestionsArea.setText("No suggestions found for \"" + input + "\".");
                } else {
                    suggestionsArea.setText("Suggestions: \n" + String.join("\n", suggestions));
                }
            }
        });

        return wordCompletionPanel;
    }

    // Word Completion Using Tries
    // Find word completions for a given input
    // Load the vocabulary into a Trie.
    // Find all words in the Trie that start with the given prefix input.
    // A list of words that start with the given prefix.
    public static List<String> getWordCompletions(String input) {
        WordCompletion trie = new WordCompletion();

        //vocabulary
        String csvFile = "fido.csv";
        String line;
        String delimiter = ",";

        // establish trie
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // row
                String[] cells = line.split(delimiter);

                for (String cell : cells) {
                    // eliminate all the non-letter character,only save letter and change it into lower-case letter
                    String cleanedCell = cell.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();

                    // split string with single blankspace
                    String[] words = cleanedCell.trim().split("\\s+");
                    // implement the trie
                    for (String word : words) {
                        trie.insert(word); // insert the vocabulary
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        trie.prefixfinder(input);

        List<String> completions = new ArrayList<>();
        completions = trie.prefixfinder(input);
        return completions;
    }
}
