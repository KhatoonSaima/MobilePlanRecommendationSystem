package com.mac.acc.recommendation;
import java.util.List;

/** @author Aoqing Liu
 *  @since 2024-11-30
 */
//class of user's preferences, 3 main factors --data\price\other features
public class UserPreferences {
    private double desiredData;
    private double maxPrice;
    private List<String> preferences;

    public UserPreferences(double desiredData, double maxPrice, List<String> preferences) {
        this.desiredData = desiredData;
        this.maxPrice = maxPrice;
        this.preferences = preferences;
    }

    public double getDesiredData() { return desiredData; }
    public double getMaxPrice() { return maxPrice; }
    public List<String> getPreferences() { return preferences; }
}
