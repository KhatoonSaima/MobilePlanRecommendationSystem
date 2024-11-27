import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SpellCheckerUI {

    public static JPanel createPanel() {
        JPanel spellCheckingPanel = new JPanel();
        spellCheckingPanel.setLayout(new BoxLayout(spellCheckingPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField spellCheckField = new JTextField(30);
        JButton spellCheckButton = new JButton("Check Spelling");
        inputPanel.add(new JLabel("Spell Checking:"));
        inputPanel.add(spellCheckField);
        inputPanel.add(spellCheckButton);

        JLabel resultLabel = new JLabel(" ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 500, 0));

        spellCheckingPanel.add(inputPanel);
        spellCheckingPanel.add(resultLabel);

        spellCheckButton.addActionListener(e -> {
            String input = spellCheckField.getText().trim().toLowerCase();
            if (input.isEmpty()) {
                resultLabel.setText("Error: Input field is empty!");
                resultLabel.setForeground(Color.RED);
            } else {
                List<String> corrections = findCorrections(input, 5);
                if (corrections.isEmpty()) {
                    resultLabel.setText("No suggestions found.");
                    resultLabel.setForeground(Color.GREEN);
                } else {
                    resultLabel.setText("Did you mean: " + String.join(", ", corrections) + "?");
                    resultLabel.setForeground(Color.BLUE);
                }
            }
        });

        return spellCheckingPanel;
    }

    // Spell Checker using Tries
    // Method to find all words within a specified edit distance
    // Load the vocabulary into a Trie.
    // Check if the word exists in the Trie.
    // If the word does not exist, suggest alternative words based on the Edit Distance algorithm.
    public static List<String> findCorrections(String wordInput, int maxEditDistance) {
        List<String> corrections = new ArrayList<>();

        // create a new instance of trie
        SpellChecker.Trie dictionary = new SpellChecker.Trie();

        // load vocab into the trie from files
        String filename = "phoneplans.csv";

        // try catch just to make sure the file exists or is reachable
        try {
            Scanner csvScan = new Scanner(new File(filename));
            while (csvScan.hasNextLine()) {
                String currLine = csvScan.nextLine();
                String[] wordsInString = currLine.split("[^\\w.]+|(?<!\\w)\\.|\\.(?!\\w)", 0);
                for (String word : wordsInString) {
                    if (!word.trim().isEmpty()) {
                        // Convert the word to lowercase and insert into the Trie
                        dictionary.insertWord(word.toLowerCase());
                    }
                }
            }
            csvScan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            // return; // Exit if the file cannot be found
        }

        // Convert to lowercase and check if the word exists in the Trie
        if (dictionary.searchWord(wordInput.toLowerCase())) {
            System.out.println(wordInput + " was found in the dictionary!");
        } else {
            // If not found, suggest similar words
            corrections = dictionary.suggestSimilarWords(dictionary, wordInput.toLowerCase(), maxEditDistance);
        }

        return corrections;
    }
}
