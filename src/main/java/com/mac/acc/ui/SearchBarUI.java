package com.mac.acc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SearchBarUI {

    public static JPanel createPanel() {
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);

        // Panel to display word completions
        JPanel suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new BoxLayout(suggestionsPanel, BoxLayout.Y_AXIS));
        suggestionsPanel.setVisible(false); // Initially hidden
        searchBarPanel.add(inputPanel);
        searchBarPanel.add(suggestionsPanel);

        JLabel resultLabel = new JLabel(" ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 700, 0));
        searchBarPanel.add(resultLabel);

        // Word completion logic as you type
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                showCompletionSuggestions(searchField, suggestionsPanel);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                showCompletionSuggestions(searchField, suggestionsPanel);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                showCompletionSuggestions(searchField, suggestionsPanel);
            }
        });

        // Action when Enter key is pressed
        searchButton.addActionListener(e -> checkForSpellingError(searchField, resultLabel));

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkForSpellingError(searchField, resultLabel);
                }
            }
        });

        return searchBarPanel;
    }

    private static void showCompletionSuggestions(JTextField searchField, JPanel suggestionsPanel) {
        String input = searchField.getText().trim().toLowerCase();

        // Clear previous suggestions
        suggestionsPanel.removeAll();

        // Check for completions as you type
        if (!input.isEmpty()) {
            List<String> completions = SearchBar.getWordCompletions(input);
            if (!completions.isEmpty()) {
                // Limit to the closest 3 matches
                completions.stream().limit(3).forEach(word -> {
                    JLabel suggestionLabel = new JLabel(word);
                    suggestionLabel.setForeground(Color.BLUE);
                    suggestionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    suggestionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            searchField.setText(word);  // Set the word on click
                        }
                    });
                    suggestionsPanel.add(suggestionLabel);
                });
                suggestionsPanel.setVisible(true);
            } else {
                suggestionsPanel.setVisible(false);
            }
        } else {
            suggestionsPanel.setVisible(false);
        }

        // Refresh the panel
        suggestionsPanel.revalidate();
        suggestionsPanel.repaint();
    }

    private static void checkForSpellingError(JTextField searchField, JLabel resultLabel) {
        String input = searchField.getText().trim().toLowerCase();

        if (input.isEmpty()) {
            resultLabel.setText("Error: Input field is empty!");
            resultLabel.setForeground(Color.RED);
        } else {
            List<String> corrections = SearchBar.findCorrections(input, 5);
            if (corrections.isEmpty()) {
                resultLabel.setText("No suggestions found.");
                resultLabel.setForeground(Color.ORANGE);
            } else {
                resultLabel.setText("Did you mean: " + String.join(", ", corrections) + "?");
                resultLabel.setForeground(Color.BLUE);
            }
        }
    }
}
