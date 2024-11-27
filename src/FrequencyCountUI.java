import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class FrequencyCountUI {

    public static JPanel createPanel() {
        JPanel frequencyCountPanel = new JPanel(new FlowLayout());
        JLabel resultLabelFrequencyCount = new JLabel(" ");
        resultLabelFrequencyCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        frequencyCountPanel.add(resultLabelFrequencyCount);
        return frequencyCountPanel;
    }

    // Frequency Count Using Quick sort
    // Parse the text from merged CSV files to extract words.
    // Use a hash table to count the occurrences of each word.
    // Use a sorting algorithm quick sort to sort the words based on their frequency.
    // Display the top 10 most frequent words to the user.
    public static void displayWordFrequencies(JPanel panel, int N) throws FileNotFoundException {
        JLabel resultLabel = (JLabel) panel.getComponent(0);
        try {
            Scanner sc = new Scanner(new File("./MergedCSV_File.csv"));
            sc.useDelimiter(",");

            Hashtable<String, Integer> wordsCount = new Hashtable<>();

            FrequencyCount.readFile(sc, wordsCount);
            List<Map.Entry<String, Integer>> list = new LinkedList<>(wordsCount.entrySet());
            list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            StringBuilder result = new StringBuilder("<html>Top " + N + " Most Frequent Words:<br>");
            for (int i = 0; i < N && i < list.size(); i++) {
                Map.Entry<String, Integer> entry = list.get(i);
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append("<br>");
            }
            result.append("</html>");
            resultLabel.setText(result.toString());
            sc.close();
        } catch (FileNotFoundException ex) {
            resultLabel.setText("Error: File not found.");
            ex.printStackTrace();
        }
    }
}
