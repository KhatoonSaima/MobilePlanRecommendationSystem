package com.mac.acc.recommendation;
import java.util.ArrayList;
import java.util.List;

/** @author Aoqing Liu
 *  @since 2024-11-30
 */
//definition of a package(or plan)
public class Package {
    private String name;
    private String brand;
    private double dataLimit;
    private double price;
    private List<String> features;

    public Package(String name,String brand, double dataLimit, double price, List<String> features) {
        this.name = name;
        this.brand = brand;
        this.dataLimit = dataLimit;
        this.price = price;
        this.features = features != null ? features : new ArrayList<>();
    }
    //getter
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getDataLimit() { return dataLimit; }
    public double getPrice() { return price; }
    public List<String> getFeatures() { return features; }
}
