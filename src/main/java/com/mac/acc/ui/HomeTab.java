package com.mac.acc.ui;
import com.mac.acc.datacomparison.DataComparison;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

public class HomeTab extends JPanel {

    public HomeTab() {
        // Set the layout of the panel to BorderLayout
        setLayout(new BorderLayout());

        // Create a JLabel for the header text, centered, with specific font
        JLabel homeLabel = new JLabel("Get the best plan based on data or price", SwingConstants.CENTER);
        homeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        //homeLabel.setForeground(Color.BLUE);

        // Add the JLabel to the top of the panel (NORTH position)
        add(homeLabel, BorderLayout.NORTH);

        // Create a JPanel to hold the buttons
        JPanel buttonPanel = new JPanel();

        // Create two buttons: one for Data and another for Price
        JButton dataButton = new JButton("Data");
        JButton priceButton = new JButton("Price");

        // Set a larger preferred size for the buttons
        dataButton.setPreferredSize(new Dimension(150, 50));  // Width 200, Height 50
        priceButton.setPreferredSize(new Dimension(150, 50)); // Width 200, Height 50

        // Increase the font size to make the text inside the button larger
        dataButton.setFont(new Font("Arial", Font.PLAIN, 15));  // Larger font size
        priceButton.setFont(new Font("Arial", Font.PLAIN, 15)); // Larger font size

        // Create a JEditorPane to display the recommended plan, with set dimensions
        JEditorPane outputArea = new JEditorPane(); // Enable line wrapping for the text area
        outputArea.setContentType("text/html"); // Enable HTML rendering
        outputArea.setEditable(false);  // Make the output area non-editable

        // Add a titled border
        outputArea.setBorder(BorderFactory.createTitledBorder("Recommended Plan"));

        // Add buttons to the button panel
        buttonPanel.add(dataButton);
        buttonPanel.add(priceButton);

        // Add the button panel to the center of the layout
        add(buttonPanel, BorderLayout.CENTER);

        // Add the JEditorPane (wrapped in a JScrollPane) to the bottom of the panel (SOUTH position)
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Make JEditorPane larger
        outputArea.setPreferredSize(new Dimension(600, 400)); // Adjust width and height as needed


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

        // Add hyperlink listener for clickable links
        outputArea.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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


