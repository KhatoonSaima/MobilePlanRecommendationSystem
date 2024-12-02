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
        initializeUI();
        loadPlansFromCSV();
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
                "5. Automatic Payment Discount</html>"), gbc);
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

    private void loadPlansFromCSV() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("mobile_plans.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            System.out.println("开始加载CSV文件...");
            String line;
            boolean firstLine = true;
            Map<String, Integer> headers = new HashMap<>();
            int count = 0;

            while ((line = reader.readLine()) != null) {
                List<String> values = parseCSVLine(line);

                if (firstLine) {
                    // 处理表头，记录每个列的索引
                    for (int i = 0; i < values.size(); i++) {
                        headers.put(values.get(i).trim().toLowerCase(), i);
                    }
                    System.out.println("表头映射: " + headers);
                    firstLine = false;
                    continue;
                }

                try {
                    // 获取基本信息
                    String name = getValue(values, headers, "plan name");
                    double price = parsePrice(getValue(values, headers, "plan cost"));
                    double data = parseData(getValue(values, headers, "data"));

                    // 收集特性
                    List<String> features = new ArrayList<>();

                    // 检查 talk time 和 text time 是否包含 unlimited
                    String talkTime = getValue(values, headers, "talk time");
                    String textTime = getValue(values, headers, "text time");

                    if (talkTime.toLowerCase().contains("unlimited")) {
                        features.add("Unlimited Talk Time");
                    }
                    if (textTime.toLowerCase().contains("unlimited")) {
                        features.add("Unlimited Text");
                    }

                    // 检查其他特性
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
                        features.add("Automatic Payment Discount");
                    }

                    // 创建并添加套餐
                    System.out.println("\n处理套餐: " + name);
                    System.out.println("价格: $" + price);
                    System.out.println("数据: " + data + "GB");
                    System.out.println("特性: " + features);

                    recommender.addPackage(new Package(name, data, price, features));
                    count++;

                } catch (Exception e) {
                    System.out.println("处理行时出错: " + line);
                    System.out.println("错误信息: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("\nCSV文件加载完成，成功加载 " + count + " 条记录");
            recommender.printAllPackages();

        } catch (IOException e) {
            System.out.println("加载CSV文件时出错:");
            e.printStackTrace();
        }
    }

    // 获取CSV中某一列的值，如果不存在或为空返回空字符串
    private String getValue(List<String> values, Map<String, Integer> headers, String columnName) {
        Integer index = headers.get(columnName.toLowerCase());
        if (index != null && index < values.size()) {
            String value = values.get(index).trim();
            // 去除引号
            value = value.replaceAll("^\"|\"$", "");
            return value;
        }
        return "";
    }

    private double parsePrice(String price) {
        if (price.isEmpty()) return 0.0;

        System.out.println("正在解析价格: " + price);
        try {
            // 先找到 $ 后面的数字部分，再去掉 /mo. 部分
            String[] parts = price.split("/");
            String numberPart = parts[0].replace("$", "").trim();
            System.out.println("提取的数字部分: " + numberPart);
            return Double.parseDouble(numberPart);
        } catch (Exception e) {
            System.out.println("价格解析失败: " + price);
            return 0.0;
        }
    }
    private double parseData(String data) {
        if (data == null || data.isEmpty() || data.toLowerCase().contains("no data")) {
            return 0.0;
        }

        System.out.println("正在解析数据量: " + data);
        try {
            // 使用正则表达式匹配数字后跟GB或MB的模式
            Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)[\\s]*(GB|MB)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                double amount = Double.parseDouble(matcher.group(1));
                String unit = matcher.group(2).toLowerCase();

                // 如果是MB，转换为GB
                if (unit.equals("mb")) {
                    amount = amount / 1024;
                }

                System.out.println("解析结果: " + amount + " GB");
                return amount;
            }

            System.out.println("未找到有效的数据量表示，返回0");
            return 0.0;
        } catch (Exception e) {
            System.out.println("数据量解析失败: " + data);
            return 0.0;
        }
    }

    // 解析CSV行，处理引号内的逗号
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
                    case "5" -> features.add("Automatic Payment Discount");
                }
            }
        }
        return features;
    }

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
            output.append(String.format("Plan: %s\n", plan.getName()));
            output.append(String.format("Data: %.1f GB\n", plan.getDataLimit()));
            output.append(String.format("Price: $%.2f/month\n", plan.getPrice()));
            output.append("Features: ").append(String.join(", ", plan.getFeatures())).append("\n\n");
        }

        outputArea.setText(output.toString());
    }
}

