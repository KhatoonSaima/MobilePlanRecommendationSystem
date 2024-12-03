package com.mac.acc.ui;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupportTab extends JPanel {

    public SupportTab() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Support For You 🚑", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Email Address
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email Address *"), gbc);
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Mobile Number
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Mobile Number *"), gbc);
        JTextField mobileField = new JTextField(20);
        gbc.gridx = 1;
        add(mobileField, gbc);

        // Complaint
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Your Complaint *"), gbc);
        JTextArea complaintArea = new JTextArea(5, 20);
        complaintArea.setLineWrap(true);
        complaintArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(complaintArea), gbc);

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
            String email = emailField.getText().trim();
            String mobile = mobileField.getText().trim();
            String complaint = complaintArea.getText().trim();

            if (email.isEmpty() || mobile.isEmpty() || complaint.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!isValidEmailAddress(email)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Email Address!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (!isValidPhoneNumber(mobile)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid phone number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    // Write to file complaints.txt
                    writeToFile(email, mobile, complaint);
                    JOptionPane.showMessageDialog(null, "Your complaint has been submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    emailField.setText("");
                    mobileField.setText("");
                    complaintArea.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Method to validate phone numbers
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regular expression for phone numbers (e.g., 1234567890, (123) 456-7890, +1 123 456 7890, etc.)
        String phoneNumberRegex = "\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*";
        Pattern phonePattern = Pattern.compile(phoneNumberRegex);
        Matcher searchMatcher = phonePattern.matcher(phoneNumber);
        return searchMatcher.matches();
    }

    // Method to validate email addresses
    private boolean isValidEmailAddress(String emailAddress) {
        // Regular expression for Email addresses
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher searchMatcher = emailPattern.matcher(emailAddress);
        //return emailAddress.matches(emailRegex);
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
        catch (IOException e) {
            // Handle IOException (e.g., file write error)
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}

