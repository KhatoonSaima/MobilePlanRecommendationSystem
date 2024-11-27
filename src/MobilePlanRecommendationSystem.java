import javax.swing.*;

public class MobilePlanRecommendationSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mobile Plan Recommendation System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Tabbed Pane
            JTabbedPane tabbedPane = new JTabbedPane();

            // Home Tab
            tabbedPane.addTab("Home", new HomeTab());

            // Search Plan Tab
            tabbedPane.addTab("Search Plan", new SearchPlanTab());

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
