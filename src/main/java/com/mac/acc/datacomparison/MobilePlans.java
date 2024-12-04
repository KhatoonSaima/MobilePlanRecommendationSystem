package com.mac.acc.datacomparison;

import java.util.HashMap;

public class MobilePlans {
	
	public HashMap<String,String> plan;  //Stores the plan data
	public String provider;
    public String link;
	
    public MobilePlans(String provider, String link, HashMap<String,String> plan) {
    	this.provider = provider;
    	this.link = link;
    	this.plan= new HashMap<>(plan);;  //Complete plan details e.g: (Data, 50 GB), (Cost, $35)
	}
    
    public String getProvider() {
        return provider;
    }

    

    public HashMap<String, String> getPlan() {
        return plan;
    }
    
    // Parse numeric cost from the plan
    public double getCost() {
        String costStr = plan.getOrDefault("Plan Cost", "0").replaceAll("[^0-9.]", ""); // Remove non-numeric characters
        return costStr.isEmpty() ? 0.0 : Double.parseDouble(costStr);
    }

    // Parse numeric data from the plan
    public double getDataLimit() {
        //For GB. e.g, 50 GB will parse into 50.00
        if(plan.getOrDefault("Data", "0").toLowerCase().contains("gb")) {  
        	String dataStr = plan.getOrDefault("Data", "0").replaceAll("[^0-9.]", ""); // Remove non-numeric characters
            return dataStr.isEmpty() ? 0.0 : Double.parseDouble(dataStr);
        }
        //For MB. e.g, 1000 MB will parse into 0.9765625
        else{  
        	String dataStr = plan.getOrDefault("Data", "0").replaceAll("[^0-9.]", "");
        	return dataStr.isEmpty() ? 0.0 : Double.parseDouble(dataStr)*(1/1024);
        }
    }
    
    
}
