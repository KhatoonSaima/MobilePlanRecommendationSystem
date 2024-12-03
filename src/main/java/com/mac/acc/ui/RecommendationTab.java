package com.mac.acc.ui;

import com.mac.acc.recommendation.Package;
import com.mac.acc.recommendation.PackageRecommender;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecommendationTab extends JPanel {
    private PackageRecommender recommender;
    private JTextArea outputArea;

    public RecommendationTab() {
        recommender = new PackageRecommender();
        initializeUI(); //UI
        loadPlansFromCSV(); // INPUT
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Hi! I can recommend plans based on your preference.📱", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Budget Field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("💰 What is your budget ($/month)?"), gbc);
        JTextField budgetField = new JTextField(20);
        gbc.gridx = 1;
        add(budgetField, gbc);

        // Data Amount Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("📊 How much data do you need (GB/month)?"), gbc);
        JTextField dataField = new JTextField(20);
        gbc.gridx = 1;
        add(dataField, gbc);

        // Features Selection
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("<html>🌍 Select desired features (comma-separated numbers):<br>" +
                "1. Unlimited Talk Time<br>" +
                "2. Unlimited Text<br>" +
                "3. International Calls<br>" +
                "4. Roaming<br>" +
                "5. Discount</html>"), gbc);
        JTextField featureField = new JTextField(20);
        gbc.gridx = 1;
        add(featureField, gbc);

        // Output Area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Submit Button
        JButton submitButton = new JButton("Find Best Plans");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(submitButton, gbc);

        // Action Listener for submit button
        submitButton.addActionListener(e -> {
            try {
                double budget = Double.parseDouble(budgetField.getText().trim().replaceAll("\\$", ""));
                double data = Double.parseDouble(dataField.getText().trim().replaceAll("GB", ""));
                String featureInput = featureField.getText().trim();

                List<String> selectedFeatures = parseFeatures(featureInput);

                if (validateInput(budget, data, featureInput)) {
                    List<Package> recommendations = recommender.recommendTopK(data, budget, selectedFeatures);
                    displayRecommendations(recommendations);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers for budget and data amount",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    //dealing with the CSV file
    private void loadPlansFromCSV() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("mobile_plans.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            System.out.println("start loading file...");
            String line;
            boolean firstLine = true;
            Map<String, Integer> headers = new HashMap<>();
            int count = 0;

            while ((line = reader.readLine()) != null) {
                List<String> values = parseCSVLine(line);

                if (firstLine) {
                    // dealing with the header,record the index of each column
                    for (int i = 0; i < values.size(); i++) {
                        headers.put(values.get(i).trim().toLowerCase(), i);
                    }
                    System.out.println("Header: " + headers);
                    firstLine = false;
                    continue;
                }

                try {
                    // get the basic info
                    String brand = getValue(values, headers, "brand");
                    String name = getValue(values, headers, "plan name");
                    double price = parsePrice(getValue(values, headers, "plan cost"));
                    double data = parseData(getValue(values, headers, "data"));

                    // collect features
                    List<String> features = new ArrayList<>();

                    // check the string if it's contains the word -- unlimited
                    String talkTime = getValue(values, headers, "talk time");
                    String textTime = getValue(values, headers, "text time");

                    if (talkTime.toLowerCase().contains("unlimited")) {
                        features.add("Unlimited Talk Time");
                    }
                    if (textTime.toLowerCase().contains("unlimited")) {
                        features.add("Unlimited Text");
                    }

                    // exam other features
                    String internationalCall = getValue(values, headers, "international call time");
                    String roaming = getValue(values, headers, "roaming");
                    String discount = getValue(values, headers, "discount");

                    if (!internationalCall.isEmpty()) {
                        features.add("International Calls");
                    }
                    if (!roaming.isEmpty()) {
                        features.add("Roaming");
                    }
                    if (!discount.isEmpty()) {
                        features.add("Discount");
                    }

                    // insert the plan
                    System.out.println("\ndealing the package: " + name);
                    System.out.println("Brand: " + brand);
                    System.out.println("Price: $" + price);
                    System.out.println("Data: " + data + "GB");
                    System.out.println("Features: " + features);

                    recommender.addPackage(new Package(name,brand, data, price, features));
                    count++;

                } catch (Exception e) {
                    System.out.println("error occur when dealing with the column: " + line);
                    System.out.println("false info: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("\nFinish the loading of the csv file，success loading " + count + " records");
            recommender.printAllPackages();

        } catch (IOException e) {
            System.out.println("Error occurs when loading the csv file :");
            e.printStackTrace();
        }
    }

    //  get the value of a column from the given csv file, if it's "-" then return null
    private String getValue(List<String> values, Map<String, Integer> headers, String columnName) {
        Integer index = headers.get(columnName.toLowerCase());
        if (index != null && index < values.size()) {
            String value = values.get(index).trim();
            // remove quotation marks
            value = value.replaceAll("^\"|\"$", "");
            return value;
        }
        return "";
    }
   // dealing with the price info
    private double parsePrice(String price) {
        if (price.isEmpty()) return 0.0;

        System.out.println("analysing: " + price);
        try {
            // find the number part first then remove the "/mo." part
            String[] parts = price.split("/");
            String numberPart = parts[0].replace("$", "").trim();
            System.out.println("number part: " + numberPart);
            return Double.parseDouble(numberPart);
        } catch (Exception e) {
            System.out.println("fail to analysis: " + price);
            return 0.0;
        }
    }
    //dealing with the data info
    private double parseData(String data) {
        if (data == null || data.isEmpty() || data.toLowerCase().contains("no data")) {
            return 0.0;
        }else if (data == "Unlimited"){
            return Double.POSITIVE_INFINITY;
        }

        System.out.println("analysing: " + data);
        try {
            // using regular expression to match the number with GB/MB
            Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)[\\s]*(GB|MB)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                double amount = Double.parseDouble(matcher.group(1));
                String unit = matcher.group(2).toLowerCase();

                // if it's MB,convert it to GB
                if (unit.equals("mb")) {
                    amount = amount / 1024;
                }

                System.out.println("The result of analysis: " + amount + " GB");
                return amount;
            }

            System.out.println("can't find the valid data amount，return 0");
            return 0.0;
        } catch (Exception e) {
            System.out.println("fail to analysis data: " + data);
            return 0.0;
        }
    }

    //analysis a row from csv file
    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString().trim());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        result.add(field.toString().trim());
        return result;
    }


    private List<String> parseFeatures(String input) {
        List<String> features = new ArrayList<>();
        if (!input.isEmpty()) {
            String[] numbers = input.split(",");
            for (String number : numbers) {
                switch (number.trim()) {
                    case "1" -> features.add("Unlimited Talk Time");
                    case "2" -> features.add("Unlimited Text");
                    case "3" -> features.add("International Calls");
                    case "4" -> features.add("Roaming");
                    case "5" -> features.add("Discount");
                }
            }
        }
        return features;
    }

    // validate the input
    private boolean validateInput(double budget, double data, String features) {
        if (budget <= 0 || data <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Budget and data amount must be positive numbers",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!features.isEmpty() && !features.matches("^[1-5](,[1-5])*$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid feature numbers (1-5) separated by commas",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void displayRecommendations(List<Package> recommendations) {
        StringBuilder output = new StringBuilder();
        output.append("Recommended Plans:\n\n");

        for (int i = 0; i < recommendations.size(); i++) {
            Package plan = recommendations.get(i);
            output.append(String.format("Recommendation #%d:\n", i + 1));
            output.append(String.format("Brand: %s\n", plan.getBrand()));
            output.append(String.format("Plan: %s\n", plan.getName()));
            output.append(String.format("Data: %.1f GB\n", plan.getDataLimit()));
            output.append(String.format("Price: $%.2f/month\n", plan.getPrice()));
            output.append("Features: ").append(String.join(", ", plan.getFeatures())).append("\n\n");
        }

        outputArea.setText(output.toString());
    }
}

