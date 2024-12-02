package com.mac.acc.recommendation;

// 需要确保明确指定类型参数
public class ScoredPackage implements Comparable<ScoredPackage> {
    private double score;
    private com.mac.acc.recommendation.Package pkg;

    public ScoredPackage(double score, com.mac.acc.recommendation.Package pkg) {
        this.score = score;
        this.pkg = pkg;
    }

    public Package getPackage() { return pkg; }
    public double getScore() { return score; }

    @Override
    public int compareTo(ScoredPackage other) {
        // 确保参数类型匹配
        return Double.compare(this.score, other.score);
    }
}