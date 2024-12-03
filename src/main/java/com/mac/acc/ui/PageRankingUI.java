package com.mac.acc.ui;
import com.mac.acc.search.Field;
import com.mac.acc.search.SearchEngineSingleton;
import com.mac.acc.search.engine.FieldCondition;
import com.mac.acc.search.engine.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class PageRankingUI {
    private static class SearchField {
        JTextField searchBox;
        JToggleButton scopeButton;
        JToggleButton caseButton;

        public SearchField() {
            searchBox = new JTextField(30);
            scopeButton = new JToggleButton("Tight");
            caseButton = new JToggleButton("Case-insensitive");

            // Configure toggle buttons
            scopeButton.addActionListener(e ->
                    scopeButton.setText(scopeButton.isSelected() ? "Loose" : "Tight"));
            caseButton.addActionListener(e ->
                    caseButton.setText(caseButton.isSelected() ? "Case-sensitive" : "Case-insensitive"));
        }
    }

    public static JPanel createPanel() {
        JPanel pageRankingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Field names
        String[] fieldNames = {
                "Plan details", "Data allowance", "Additional feature",
                "Family plan & discounts", "Overcharge fees"
        };

        // Create search fields
        List<SearchField> searchFields = new ArrayList<>();

        // Add header labels
        gbc.gridy = 0;
        gbc.gridx = 0; addHeaderLabel(pageRankingPanel, "Field Name", gbc);
        gbc.gridx = 1; addHeaderLabel(pageRankingPanel, "Search Box", gbc);
        gbc.gridx = 2; addHeaderLabel(pageRankingPanel, "Scope", gbc);
        gbc.gridx = 3; addHeaderLabel(pageRankingPanel, "Case Sensitivity", gbc);

        // Add fields
        for (int i = 0; i < fieldNames.length; i++) {
            gbc.gridy = i + 1;
            SearchField field = new SearchField();
            searchFields.add(field);

            gbc.gridx = 0;
            pageRankingPanel.add(new JLabel(fieldNames[i]), gbc);

            gbc.gridx = 1;
            pageRankingPanel.add(field.searchBox, gbc);

            gbc.gridx = 2;
            pageRankingPanel.add(field.scopeButton, gbc);

            gbc.gridx = 3;
            pageRankingPanel.add(field.caseButton, gbc);
        }

        // Output Area
        JTextArea outputArea = new JTextArea(20, 60);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBorder(BorderFactory.createTitledBorder("Page Ranking with Frequency Count: "));

        // Rank Pages button
        JButton rankButton = new JButton("Search");
        rankButton.addActionListener(e -> {
            List<Field> searchCriteria = new ArrayList<>();
            for (SearchField field : searchFields) {
                String searchText = field.searchBox.getText().trim();
                if (!searchText.isEmpty()) {
                    FieldCondition scope = field.scopeButton.isSelected() ?
                            FieldCondition.LOOSE : FieldCondition.TIGHT;
                    boolean caseSensitive = field.caseButton.isSelected();
                    searchCriteria.add(new Field(searchText, scope, caseSensitive));
                }
            }

            if (searchCriteria.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter at least one search term",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String rankingResult = performRanking(searchCriteria);
            outputArea.setText(rankingResult);
        });

        // Add button and output area
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        pageRankingPanel.add(rankButton, gbc);

        gbc.gridy++;
        pageRankingPanel.add(new JScrollPane(outputArea), gbc);

        return pageRankingPanel;
    }

    private static void addHeaderLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label, gbc);
    }

    private static String performRanking(List<Field> fields) {
        try {
            SearchEngine searchEngine = SearchEngineSingleton.getInstance();
            List<Map.Entry<String, Integer>> results = searchEngine.search(fields);

            StringBuilder resultMessage = new StringBuilder();
            for (Map.Entry<String, Integer> entry : results) {
                resultMessage.append(entry.getKey())
                        .append(" - Rank: ")
                        .append(entry.getValue())
                        .append("\n");
            }

            return resultMessage.toString();
        } catch (Exception e) {
            return "Error performing ranking: " + e.getMessage();
        }
    }
}