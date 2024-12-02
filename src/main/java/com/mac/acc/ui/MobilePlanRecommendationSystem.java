package com.mac.acc.ui;
import javax.swing.*;
import java.awt.*;

public class MobilePlanRecommendationSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Frame window size
            frame.setSize(1000, 800);

            // Create a panel to hold the title label
            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(new Color(50, 50, 50)); // Dark background for the title panel

            // Create a label for the title
            JLabel titleLabel = new JLabel("Mobile Plan Recommendation System", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);

            // Add the label to the title panel
            titlePanel.add(titleLabel);

            // Set the layout of the frame to BorderLayout
            frame.setLayout(new BorderLayout());

            // Add the title panel at the top of the frame
            frame.add(titlePanel, BorderLayout.NORTH);

            // Tabbed Pane
            JTabbedPane tabbedPane = new JTabbedPane();

            // Set font size for tab labels
            tabbedPane.setFont(new Font("Arial", Font.PLAIN, 16));

            // Home Tab
            tabbedPane.addTab("Home", new HomeTab());

            // Recommendation Tab
            tabbedPane.addTab("Recommendation", new RecommendationTab());

            // Features Tab
            tabbedPane.addTab("Features", new Features());

            // Support Tab
            tabbedPane.addTab("Support", new SupportTab());

            // Customize the tabbed pane UI (optional) to make it look cleaner
            tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
                @Override
                protected void installDefaults() {
                    super.installDefaults();
                    tabInsets = new Insets(7, 7, 7, 7);  // Remove excessive padding
                    tabAreaInsets = new Insets(7, 7, 7, 7);  // Adjust tab area insets
                }
            });

            // Add TabbedPane to Frame
            frame.add(tabbedPane, BorderLayout.CENTER);

            // Set frame visibility
            frame.setVisible(true);
        });
    }
}
