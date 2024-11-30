package com.mac.acc.ui;
import javax.swing.*;
import java.awt.*;

public class PageRankingUI {

    public static JPanel createPanel() {
        JPanel pageRankingPanel = new JPanel(new FlowLayout());
        JTextField pageRankingField = new JTextField(30);
        JButton pageRankingButton = new JButton("Rank Pages");
        pageRankingPanel.add(new JLabel("Page Ranking:"));
        pageRankingPanel.add(pageRankingField);
        pageRankingPanel.add(pageRankingButton);

        pageRankingButton.addActionListener(e -> {
            String input = pageRankingField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a query for page ranking!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                rankPage(input);
                JOptionPane.showMessageDialog(null, "Page ranking for \"" + input + "\": [ranking logic]", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return pageRankingPanel;
    }

    public static void rankPage(String input) {
        // code for page ranking
    }
}

