package com.mac.acc.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecommendationTab extends JPanel {

    private JLabel resultLabel;

    public RecommendationTab() {
        setLayout(new FlowLayout());

        // Create a button for search action
        JButton searchButton = new JButton("Search");
        add(searchButton);

        // action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method when the button is clicked
                getPlansSelectedCarrier();
            }
        });
    }


    // Function to recommend plan
    private void getPlansSelectedCarrier() {

    }
}
