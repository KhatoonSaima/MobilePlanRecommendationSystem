package com.mac.acc.ui;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecommendationTab extends JPanel {

    public RecommendationTab() {
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
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // budget
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("💰What is your budget?<br> (Please enter a number) *"), gbc);
        JTextField budgetField = new JTextField(20);
        gbc.gridx = 1;
        add(budgetField, gbc);

        // data amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("<html>📊 How much data amount(GB/MB) do you need?<br>   Just give me a rough number.</html>"), gbc);
        JTextField dataField = new JTextField(20);
        gbc.gridx = 1;
        add(dataField, gbc);

        //
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("<html>🌍 Which of the following features do you want?<br><ul>" +
                "<li>Feature 1: Unlimited Talk Time</li>" +
                "<li>Feature 2: Unlimited Text Time</li>" +
                "<li>Feature 3: International calls</li>" +
                "<li>Feature 4: Roaming</li>" +
                "<li>Feature 5: Discount</li>" +
                "</ul>Please enter the corresponding number(s) with \", \" separator</html>"), gbc);
        JTextArea featureArea = new JTextArea(5, 20);
        featureArea.setLineWrap(true);
        featureArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(featureArea), gbc);

        // Buttons Panel
        JPanel supportButtonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        supportButtonPanel.add(submitButton);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(supportButtonPanel, gbc);

        // Action Listener for submit button
        submitButton.addActionListener(e -> {
            String budget = budgetField.getText().trim();
            String data = dataField.getText().trim();
            String complaint = featureArea.getText().trim();

            if (budget.isEmpty() || data.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a your buget and data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!isValidBudget(budget)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Budget!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!isValidData(data)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid data amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!isValidFeatures(data)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid feature list!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    // Write to file complaints.txt
                    writeToFile(budget, data, complaint);
                    JOptionPane.showMessageDialog(null, "Your complaint has been submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Method to validate data amount
    private boolean isValidData(String phoneNumber) {
        // Regular expression for phone numbers (e.g., 1234567890, (123) 456-7890, +1 123 456 7890, etc.)
        String phoneNumberRegex = "^[0-9]+\\s?(GB|gb|MB|mb)?$";
        Pattern phonePattern = Pattern.compile(phoneNumberRegex);
        Matcher searchMatcher = phonePattern.matcher(phoneNumber);
        return searchMatcher.matches();
    }

    // Method to validate budget
    private boolean isValidBudget(String budget) {
        // Regular expression for budget
        String budgetRegex = "^[0-9]+\\s?\\$?$";
        Pattern budgetPattern = Pattern.compile(budgetRegex);
        Matcher searchMatcher = budgetPattern.matcher(budget);
        //return budget.matches(budgetRegex);
        return searchMatcher.matches();
    }

    // Method to validate budget
    private boolean isValidFeatures(String budget) {
        // Regular expression for budget
        String budgetRegex = "^([1-5](,[1-5])*)?$";
        Pattern budgetPattern = Pattern.compile(budgetRegex);
        Matcher searchMatcher = budgetPattern.matcher(budget);
        //return budget.matches(budgetRegex);
        return searchMatcher.matches();
    }

    // Method to write data to a file
    private void writeToFile(String email, String mobile, String complaint) throws IOException {
        String fileName = "complaints.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) { // Append mode
            writer.write("Email: " + email);
            writer.newLine();
            writer.write("Phone: " + mobile);
            writer.newLine();
            writer.write("Complaint: " + complaint);
            writer.newLine();
            writer.write("----------------------------");
            writer.newLine();
        }
    }
}

