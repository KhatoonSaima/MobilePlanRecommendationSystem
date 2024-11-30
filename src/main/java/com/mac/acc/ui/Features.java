package com.mac.acc.ui;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Features extends JPanel {

    public Features() {
        setLayout(new BorderLayout());

        // Dropdown for feature selection
        JComboBox<String> featureDropdown = new JComboBox<>(new String[]{
                "Word Completion", "Spell Checking", "Frequency Count", "Page Ranking", "Search Bar Test"
        });

        // Main content panel with CardLayout
        JPanel featureContentPanel = new JPanel(new CardLayout());

        // Word Completion Panel
        JPanel wordCompletionPanel = WordCompletionUI.createPanel();

        // Spell Checking Panel
        JPanel spellCheckingPanel = SpellCheckerUI.createPanel();

        // Frequency Count Panel
        JPanel frequencyCountPanel = FrequencyCountUI.createPanel();

        // Page Ranking Panel
        JPanel pageRankingPanel = PageRankingUI.createPanel();

        // Search Bar Panel
        JPanel searchBarPanel = SearchBarUI.createPanel();

        // Add individual panels to the CardLayout panel
        featureContentPanel.add(wordCompletionPanel, "Word Completion");
        featureContentPanel.add(spellCheckingPanel, "Spell Checking");
        featureContentPanel.add(frequencyCountPanel, "Frequency Count");
        featureContentPanel.add(pageRankingPanel, "Page Ranking");
        featureContentPanel.add(searchBarPanel, "Search Bar Test");

        // Action Listener for Dropdown to switch panels
        featureDropdown.addActionListener(e -> {
            CardLayout cl = (CardLayout) (featureContentPanel.getLayout());
            String selectedOption = (String) featureDropdown.getSelectedItem();
            cl.show(featureContentPanel, selectedOption); // Switch to the selected panel

            if ("Frequency Count".equals(selectedOption)) {
                try {
                    FrequencyCountUI.displayWordFrequencies(frequencyCountPanel, 10); // Number of top frequent words to display
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Add components to the Features Tab
        add(featureDropdown, BorderLayout.NORTH);
        add(featureContentPanel, BorderLayout.CENTER);
    }
}
