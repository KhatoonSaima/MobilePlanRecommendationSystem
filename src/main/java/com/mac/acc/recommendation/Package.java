package com.mac.acc.recommendation;

import java.util.ArrayList;
import java.util.List;
//definition of a package(or plan)
public class Package {
    private String name;
    private double dataLimit;
    private double price;
    private List<String> features;

    public Package(String name, double dataLimit, double price, List<String> features) {
        this.name = name;
        this.dataLimit = dataLimit;
        this.price = price;
        this.features = features != null ? features : new ArrayList<>();
    }
    //getter
    public String getName() { return name; }
    public double getDataLimit() { return dataLimit; }
    public double getPrice() { return price; }
    public List<String> getFeatures() { return features; }
}
