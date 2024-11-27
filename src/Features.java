import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Features extends JPanel {

    public Features() {

        setLayout(new BorderLayout());

        // Dropdown for feature selection
        JComboBox<String> featureDropdown = new JComboBox<>(
                new String[]{"Word Completion", "Spell Checking", "Frequency Count", "Page Ranking"}
        );

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

        // Add individual panels to the CardLayout panel
        featureContentPanel.add(wordCompletionPanel, "Word Completion");
        featureContentPanel.add(spellCheckingPanel, "Spell Checking");
        featureContentPanel.add(pageRankingPanel, "Page Ranking");
        featureContentPanel.add(frequencyCountPanel, "Frequency Count");

        // Action Listener for Dropdown to switch panels
        featureDropdown.addActionListener(e -> {
            CardLayout cl = (CardLayout) (featureContentPanel.getLayout());
            cl.show(featureContentPanel, (String) featureDropdown.getSelectedItem());

            String selectedOption = (String) featureDropdown.getSelectedItem();
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

