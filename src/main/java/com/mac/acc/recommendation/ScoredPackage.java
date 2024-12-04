package com.mac.acc.recommendation;
/** @author Aoqing Liu
 *  @since 2024-11-30
 */
// make sure it is designated type
// put the score info into the packages
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
        // make sure the variables are the same types
        return Double.compare(this.score, other.score);
    }
}