package com.mac.acc.ui;

import com.mac.acc.features.SearchBar;
import com.mac.acc.features.SearchFrequencyQuery;
import com.mac.acc.features.SimpleSearchFrequencyQuery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SearchBarUI {

    private static SearchFrequencyQuery searchFrequencyQuery = SearchFrequencyQueryFactory.getSearchFrequencyQuery("simple");

    public static JPanel createPanel() {
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.Y_AXIS));

        // Panel for input field and search button
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

        // Result label panel
        JLabel resultLabel = new JLabel(" ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 350, 0));  // Adjust bottom padding

        // Add result label first
        searchBarPanel.add(resultLabel);

        // Create frequency panel with JScrollPane for the frequency list
        JPanel frequencyPanelContainer = new JPanel();
        frequencyPanelContainer.setLayout(new BoxLayout(frequencyPanelContainer, BoxLayout.Y_AXIS));
        frequencyPanelContainer.setBorder(BorderFactory.createTitledBorder("Search Frequency"));

        JPanel frequencyPanel = new JPanel();
        frequencyPanel.setLayout(new BoxLayout(frequencyPanel, BoxLayout.Y_AXIS));
        frequencyPanel.setBackground(Color.WHITE);

        JScrollPane frequencyScrollPane = new JScrollPane(frequencyPanel);
        frequencyScrollPane.getViewport().setBackground(Color.WHITE);
        frequencyScrollPane.setPreferredSize(new Dimension(350, 200));  // Set a preferred size for the frequency box
        frequencyPanelContainer.add(frequencyScrollPane);

        searchBarPanel.add(frequencyPanelContainer);  // Add the frequency panel container below the result label

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

        // Action when "Search" button is clicked or Enter key is pressed
        searchButton.addActionListener(e -> performSearch(searchField, resultLabel, suggestionsPanel, frequencyPanel));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch(searchField, resultLabel, suggestionsPanel, frequencyPanel);
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
                            searchField.setText(word); // Set the word on click
                            suggestionsPanel.setVisible(false); // Hide suggestions
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

    private static void performSearch(JTextField searchField, JLabel resultLabel, JPanel suggestionsPanel, JPanel frequencyPanel) {
        String input = searchField.getText().trim().toLowerCase();

        // Clear suggestions when search is performed
        suggestionsPanel.setVisible(false);
        suggestionsPanel.removeAll();
        suggestionsPanel.revalidate();
        suggestionsPanel.repaint();

        // Clear previous frequency list
        frequencyPanel.removeAll();
        frequencyPanel.revalidate();
        frequencyPanel.repaint();

        if (input.isEmpty()) {
            resultLabel.setText("Error: Input field is empty!");
            resultLabel.setForeground(Color.RED);
        } else {
            // Use the updated SearchBar.handleSearch() to get the result
            String result = SearchBar.handleSearch(input, 5, 2); // Adjust maxSuggestions and maxEditDistance as needed

            // Style the result message based on its content
            if (result.startsWith("Word match found:")) {
                resultLabel.setForeground(Color.PINK); // Highlight exact matches in green
            } else if (result.startsWith("Did you mean:")) {
                resultLabel.setForeground(Color.BLUE); // Highlight corrections in blue
            } else if (result.startsWith("Word Completions:")) {
                resultLabel.setForeground(Color.BLACK); // Highlight suggestions in black
            } else {
                resultLabel.setForeground(Color.ORANGE); // For "No match found"
            }

            resultLabel.setText(result);

            // Now, display the search frequency list below the result label
            String frequencyResult = SearchBar.displaySearchFrequency();
            String[] lines = frequencyResult.split("\n");
            for (String line : lines) {
                JLabel frequencyLabel = new JLabel(line);
                frequencyPanel.add(frequencyLabel);
            }
        }

        // Refresh the frequency panel
        frequencyPanel.revalidate();
        frequencyPanel.repaint();
    }

    public static class SearchFrequencyQueryFactory {
        public static SearchFrequencyQuery getSearchFrequencyQuery(String code) {
            return new SimpleSearchFrequencyQuery();
        }
    }
}