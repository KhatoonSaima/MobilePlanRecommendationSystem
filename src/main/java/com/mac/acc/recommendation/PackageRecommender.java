package com.mac.acc.recommendation;

import java.util.*;

public class PackageRecommender {
    private List<com.mac.acc.recommendation.Package> packages;
    private static final int TOP_K = 3;  // top 3 recommends
    private Map<String, com.mac.acc.recommendation.Package> packageMap;  //hash map for searching the package ,key is the plan name, value is the package info

    public PackageRecommender() {
        this.packages = new ArrayList<>();
        this.packageMap = new HashMap<>();
    }

    //put the data into the container
    public void addPackage(com.mac.acc.recommendation.Package pkg) {
        packages.add(pkg);
        packageMap.put(pkg.getName(), pkg);
    }

    //the main recommend algorithm
    public List<com.mac.acc.recommendation.Package> recommendTopK(double desiredData, double maxPrice, List<String> preferences) {
        // pre-filter the data
        List<com.mac.acc.recommendation.Package> filteredPackages = preFilter(desiredData, maxPrice);

        ////use the priority queue maintain the top3 results,less space
        PriorityQueue<ScoredPackage> topPackages = new PriorityQueue<>(
                TOP_K, Comparator.comparingDouble(ScoredPackage::getScore));
        //calculate the score of the filtered packages
        for (com.mac.acc.recommendation.Package pkg : filteredPackages) {
            double score = calculateScore(pkg, desiredData, maxPrice, preferences);

            if (topPackages.size() < TOP_K) {
                topPackages.offer(new ScoredPackage(score, pkg));
            } else if (score > topPackages.peek().getScore()) {
                topPackages.poll();
                topPackages.offer(new ScoredPackage(score, pkg));
            }
        }
        //reverse the order
        List<com.mac.acc.recommendation.Package> result = new ArrayList<>(TOP_K);
        while (!topPackages.isEmpty()) {
            result.add(0, topPackages.poll().getPackage());
        }
        return result;
    }

    //pre-filter the data, decrease the number of packages that we need to calculate the scores
    private List<com.mac.acc.recommendation.Package> preFilter(double desiredData, double maxPrice) {
        return packages.stream()
                .filter(pkg -> pkg.getPrice() <= maxPrice)  // the price must smaller than the user's budget
                .filter(pkg -> pkg.getDataLimit() >= desiredData * 0.5) //the data amount must exceed the half of the user's need
                .filter(pkg -> pkg.getDataLimit() <= desiredData * 2)  //the data amount must not exceed the twice of the user's need
                .toList();
    }
    //the method of calculating the scores
    private double calculateScore(com.mac.acc.recommendation.Package pkg, double desiredData, double maxPrice, List<String> preferences) {
        double score = 0;
        // data amount matching
        double dataRatio = pkg.getDataLimit() / desiredData;  //if the data amount is bigger than 150%, it gets 5 points
        if (dataRatio > 1.5) {
            score += 5;
        } else if (dataRatio >= 0.8 && dataRatio <= 1.5) {  //if the data amount is around 80%-150%, it gets 4 points
            score += 4;
        } else if (dataRatio >= 0.5 && dataRatio < 0.8) {   //if the data amount is smaller than 80%, it gets 2 points
            score += 2;
        }

        //price matching
        score += (1 - pkg.getPrice() / maxPrice) * 3;

        if (preferences != null) {
            score += preferences.stream()
                    .filter(pref -> pkg.getFeatures().contains(pref)) // if it meets more features user want,then it get more points, 2 points for each
                    .count() * 2;
        }

        return score;
    }

        public com.mac.acc.recommendation.Package getPackageByName(String name) {
            return packageMap.get(name);
        }

    // 添加此方法来打印所有套餐信息
    public void printAllPackages() {
        if (packages.isEmpty()) {
            System.out.println("没有加载任何套餐数据！");
            return;
        }

        System.out.println("当前已加载的所有套餐信息：");
        System.out.println("总共加载了 " + packages.size() + " 个套餐");
        for (Package pkg : packages) {
            System.out.println("\n套餐名称: " + pkg.getName());
            System.out.println("数据流量: " + pkg.getDataLimit() + "GB");
            System.out.println("价格: $" + pkg.getPrice());
            System.out.println("特性: " + String.join(", ", pkg.getFeatures()));
            System.out.println("------------------------");
        }
    }
}
