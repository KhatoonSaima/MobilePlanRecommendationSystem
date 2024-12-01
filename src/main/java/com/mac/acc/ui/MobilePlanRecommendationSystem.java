package com.mac.acc.ui;
import javax.swing.*;

public class MobilePlanRecommendationSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mobile Plan Recommendation System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);

            // Tabbed Pane
            JTabbedPane tabbedPane = new JTabbedPane();

            // Home Tab
            tabbedPane.addTab("Home", new HomeTab());

            // Search Plan Tab
            tabbedPane.addTab("Recommendation", new RecommendationTab());

            // Features Tab
            tabbedPane.addTab("Features", new Features());

            // Support Tab
            tabbedPane.addTab("Support", new SupportTab());

            // Add TabbedPane to Frame
            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}
