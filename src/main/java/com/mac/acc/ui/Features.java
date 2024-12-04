package com.mac.acc.ui;

import javax.swing.*;
import java.awt.*;

/*
@author Saima Khatoon
@since 25-11-2024
 */
public class Features extends JPanel {

    public Features() {
        setLayout(new BorderLayout());

        // Dropdown for feature selection
        JComboBox<String> featureDropdown = new JComboBox<>(new String[]{
                "Search Pages", "Search Bar"
        });

        // Main content panel with CardLayout
        JPanel featureContentPanel = new JPanel(new CardLayout());

        // Page Ranking Panel
        JPanel pageRankingPanel = PageRankingUI.createPanel();

        // Search Bar Panel
        JPanel searchBarPanel = SearchBarUI.createPanel();

        // Add individual panels to the CardLayout panel
        featureContentPanel.add(pageRankingPanel, "Search Pages");
        featureContentPanel.add(searchBarPanel, "Search Bar");

        // Action Listener for Dropdown to switch panels
        featureDropdown.addActionListener(e -> {
            CardLayout cl = (CardLayout) (featureContentPanel.getLayout());
            String selectedOption = (String) featureDropdown.getSelectedItem();
            cl.show(featureContentPanel, selectedOption); // Switch to the selected panel
        });

        // Add components to the Features Tab
        add(featureDropdown, BorderLayout.NORTH);
        add(featureContentPanel, BorderLayout.CENTER);
    }
}
