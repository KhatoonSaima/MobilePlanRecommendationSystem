package com.mac.acc.ui;

import com.mac.acc.features.SearchBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SearchBarUI {

    public static JPanel createPanel() {

        // Create a panel with GridLayout (2 rows, 1 column)
        JPanel panel = new JPanel(new GridLayout(2, 1)); // Divides the panel into 2 halves

        // Top half
        JPanel topPanel = new JPanel();
        // Bottom half
        JPanel bottomPanel = new JPanel();

        // Add the panels to the GridLayout
        panel.add(topPanel);
        panel.add(bottomPanel);

        // Create main search bar panel with vertical layout
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

        // Add result label first
        searchBarPanel.add(resultLabel);
        topPanel.add(searchBarPanel);

        // Create frequency panel with JScrollPane for the frequency list
        JPanel frequencyPanelContainer = new JPanel();
        frequencyPanelContainer.setLayout(new BoxLayout(frequencyPanelContainer, BoxLayout.Y_AXIS));
        frequencyPanelContainer.setBorder(BorderFactory.createTitledBorder("Search Frequency"));

        JPanel frequencyPanel = new JPanel();
        frequencyPanel.setLayout(new BoxLayout(frequencyPanel, BoxLayout.Y_AXIS));
        frequencyPanel.setBackground(Color.WHITE);

        JScrollPane frequencyScrollPane = new JScrollPane(frequencyPanel);
        frequencyScrollPane.getViewport().setBackground(Color.WHITE);
        frequencyScrollPane.setPreferredSize(new Dimension(600, 200));  // Set a preferred size for the frequency box
        frequencyPanelContainer.add(frequencyScrollPane);

        bottomPanel.add(frequencyPanelContainer);  // Add the frequency panel container below the result label

        // Word completion logic as you type
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                suggestionsPanel.setVisible(false);
               resultLabel.setVisible(false);
                showCompletionSuggestions(searchField, suggestionsPanel);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                suggestionsPanel.setVisible(false);
                resultLabel.setVisible(false);
                showCompletionSuggestions(searchField, suggestionsPanel);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                suggestionsPanel.setVisible(false);
               resultLabel.setVisible(false);
                showCompletionSuggestions(searchField, suggestionsPanel);
            }
        });


        // Action when "Search" button is clicked or Enter key is pressed
        searchButton.addActionListener(e -> performSearch(searchField, resultLabel, suggestionsPanel, frequencyPanel));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    suggestionsPanel.setVisible(true);
                    resultLabel.setVisible(true);
                    performSearch(searchField, resultLabel, suggestionsPanel, frequencyPanel);
                }
            }
        });

        return panel;
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
                    JButton suggestionButton = new JButton(word);
                    suggestionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    suggestionButton.addActionListener(e -> {
                        searchField.setText(word);  // Set selected suggestion in search field
                        suggestionsPanel.setVisible(false); // Hide suggestions after selection
                    });

                    suggestionsPanel.add(suggestionButton);
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

        suggestionsPanel.setVisible(true);
        resultLabel.setVisible(true);

        // Clear suggestions when search is performed
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
}