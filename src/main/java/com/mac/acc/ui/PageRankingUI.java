package com.mac.acc.ui;
import com.mac.acc.search.Document;
import com.mac.acc.search.Field;
import com.mac.acc.search.SearchEngineFactory;
import com.mac.acc.search.engine.FieldCondition;
import com.mac.acc.search.engine.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PageRankingUI {

    public static JPanel createPanel() {
        JPanel pageRankingPanel = new JPanel(new FlowLayout());
        JTextField pageRankingField = new JTextField(30);
        JButton pageRankingButton = new JButton("Rank Pages");
        pageRankingPanel.add(new JLabel("Page Ranking:"));
        pageRankingPanel.add(pageRankingField);
        pageRankingPanel.add(pageRankingButton);

        // Output Area
        JTextArea outputArea = new JTextArea(20, 60);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBorder(BorderFactory.createTitledBorder("Page Ranking with Frequency Count: "));
        pageRankingPanel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        pageRankingPanel.add(scrollPane);


        pageRankingButton.addActionListener(e -> {
            String input = pageRankingField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a word for page ranking!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String rankingResult = rankPage(input);
                outputArea.setText(rankingResult);
                //JOptionPane.showMessageDialog(null, "Page ranking for \"" + input + "\": [ranking logic]", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return pageRankingPanel;
    }

    public static String rankPage(String input) {
        List<Document> testDocuments = Arrays.asList(
                new Document(Path.of("docs/public-mobile.html"),
                        "https://www.publicmobile.ca/en/on/plans"),
                new Document(Path.of("docs/fido-byod.html"),
//                        "https://www.fido.ca/phones/bring-your-own-device?flowType=byod"),
                        "https://www.fido.ca/phones/bring-your-own-device?icid=F_WIR_CNV_GRM6LG&flowType=byod&step=2&data=sku_plan_FPMM012JE_FPMM012JE&sku=BYOD&tier=NOTERM&talk=sku_plan_FPMM012JE_FPMM012JE"),
                new Document(Path.of("docs/rogers-plans.html"),
                        "https://www.rogers.com/plans"),
                new Document(Path.of("docs/rogers-family.html"),
                        "https://www.rogers.com/mobility/family-phones"),
                new Document(Path.of("docs/verizon-unlimited.html"),
                        "https://www.verizon.com/plans/unlimited/"),
                new Document(Path.of("docs/verizon-prepaid.html"),
                        "https://www.verizon.com/plans/prepaid/")
        );
        // code for page ranking
        SearchEngine searchEngine = SearchEngineFactory.getSearchEngine(testDocuments);

        // Create a field with loose condition looking for "talk plan"
        Field field = new Field(input, FieldCondition.LOOSE, false);
        java.util.List<Map.Entry<String, Integer>> results = searchEngine.search(List.of(field));

        // Format the ranking results into a string
        StringBuilder resultMessage = new StringBuilder();
        for (Map.Entry<String, Integer> entry : results) {
            resultMessage.append(entry.getKey()).append(" - Rank: ").append(entry.getValue()).append("\n");
        }

        return resultMessage.toString();
    }
}

