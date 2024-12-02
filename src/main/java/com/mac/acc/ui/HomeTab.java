package com.mac.acc.ui;
import com.mac.acc.datacomparison.DataComparison;

import javax.swing.*;
import java.awt.*;

public class HomeTab extends JPanel {

    public HomeTab() {
        setLayout(new BorderLayout());

        JLabel homeLabel = new JLabel("Get the best plan based on data or price", SwingConstants.CENTER);
        homeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(homeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton dataButton = new JButton("Data");
        JButton priceButton = new JButton("Price");
        JTextArea outputArea = new JTextArea(30, 30);

        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBorder(BorderFactory.createTitledBorder("Recommended Plan"));

        buttonPanel.add(dataButton);
        buttonPanel.add(priceButton);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);


        // Data Button action listener
        dataButton.addActionListener(e -> {
            // Fetch the data-based plan
            String recommendedPlan = getRecommendedPlanBasedOnData();
            // Display it in the output area
            outputArea.setText(recommendedPlan);
        });

        // Price Button action listener
        priceButton.addActionListener(e -> {
            // Fetch the price-based plan
            String recommendedPlan = getRecommendedPlanBasedOnPrice();
            // Display it in the output area
            outputArea.setText(recommendedPlan);
        });
    }

    // Function to recommend best plan based on data
    private String getRecommendedPlanBasedOnData() {
        return DataComparison.comparePlansByData();
    }

    // Function to recommend best plan based on price
    private String getRecommendedPlanBasedOnPrice() {
        return DataComparison.comparePlansByCost();
    }
}
