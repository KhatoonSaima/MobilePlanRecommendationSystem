package com.mac.acc.datacomparison;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataComparison {
	//Read the data in the file
	public static HashMap<String,String> readData(String[] data, String[] head){
		HashMap<String,String> plans = new HashMap<String,String>();
		
	    //adding the data to plans variable for comparison
		for(int i=0; i<head.length; i++) {
			plans.put(head[i], data[i]);
		}
		return plans;
	}
	//Initializing all the plans
    public static List<MobilePlans> readPlans(){
    	List<MobilePlans> allPlans = new ArrayList<MobilePlans>();
    	//Initializing all the plans
    	try {
    		
			//PriorityQueue<WebPage> rankedPages = new MobilePlansPageRanking().rankPages();
			String websites[] = {"Rogers","Fido","Freedom","PublicMobile","Verizon"};
			String link[] = {"https://www.rogers.com/plans","https://www.fido.ca/phones/bring-your-own-device?icid=F_WIR_CNV_GRM6LG&flowType=byod&step=2&data=sku_plan_FPMM012JE_FPMM012JE&sku=BYOD&tier=NOTERM&talk=sku_plan_FPMM012JE_FPMM012JE","https://shop.freedommobile.ca/en-CA/prepaid-plans", "https://www.publicmobile.ca/en/on/plans", "https://www.verizon.com/plans/prepaid/"};

			//Iterating through each webpage rank wise
			for(int w=0; w<websites.length;w++) {
				//Extracting top ranked pages
				//WebPage wp = rankedPages.poll();
				
				String provider = websites[w]; //Website name
				String links = link[w];  // Website url

				//Reading all the webpages
				//Reading the webpage csv file and storing the plans data in csv file in HashMap
				try(BufferedReader bf = new BufferedReader(new FileReader(".//HomeTabCSVFiles/"+provider+".csv"))){
					String[] head = bf.readLine().split(",");
					String row;
					
					
					//Each line in csv file represents one plan
					while((row=bf.readLine())!=null) {
						
						String data[] = row.split(",");
						
						MobilePlans m = new MobilePlans(provider, links, readData(data,head));
						allPlans.add(m);

						// Uncomment below lines to print the plans in console
						/*for(int i=0;i<allPlans.size();i++) {
				    		System.out.println(allPlans.get(i).provider+allPlans.get(i).plan.get("Plan Name")+allPlans.get(i).plan.get("Plan Cost")+allPlans.get(i).plan.get("Data"));
				    	}*/
					}
                }
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("Error in reading file "+e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return allPlans;
    }
    
    //Comparing Plans by plancost
    public static String comparePlansByCost(){

		String planDetail1 = "";
		String planDetail2 = "";
		String planDetail3 = "";
		String planDetail4 = "";

		try {
			//Read all the plans
			List<MobilePlans> plans = readPlans();

			List<MobilePlans> sortedPlans = plans.stream()
					.sorted(Comparator.comparingDouble(MobilePlans::getCost)) // Ascending order of cost
					.collect(Collectors.toList());
    	/*
    	for(MobilePlans m :sortedPlans) {
    		System.out.println(m.provider+" "+m.plan.get("Plan Name")+m.plan.get("Plan Cost")+m.plan.get("Data"));
    	}*/

			MobilePlans plan1 = sortedPlans.get(0);
			MobilePlans plan2 = sortedPlans.get(1);
			MobilePlans plan3 = sortedPlans.get(2);
			MobilePlans plan4 = sortedPlans.get(3);
			planDetail1 = "Plan Provider :" + plan1.provider + "\n" + "Plan Name: " + plan1.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan1.plan.get("Plan Cost") + "\n" + "URL: " + plan1.link;
			planDetail2 = "Plan Provider :" + plan2.provider + "\n" + "Plan Name: " + plan2.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan2.plan.get("Plan Cost") + "\n" + "URL: " + plan2.link;
			planDetail3 = "Plan Provider :" + plan3.provider + "\n" + "Plan Name: " + plan3.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan3.plan.get("Plan Cost") + "\n" + "URL: " + plan3.link;
			planDetail4 = "Plan Provider :" + plan4.provider + "\n" + "Plan Name: " + plan4.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan4.plan.get("Plan Cost") + "\n" + "URL: " + plan4.link;
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

        return planDetail1+"\n\n"+planDetail2 +"\n\n"+planDetail3 +"\n\n"+planDetail4;
    }
    
    //Comparing Plans by plandata
    public static String comparePlansByData(){

		String planDetail1 = "";
		String planDetail2 = "";
		String planDetail3 = "";
		String planDetail4 = "";

		try {
			//Read all the plans
			List<MobilePlans> plans = readPlans();


			List<MobilePlans> sortedPlans = plans.stream()
					.sorted(Comparator.comparingDouble(plan -> {
						double data = plan.getDataLimit();
						return data > 0 ? plan.getCost() / data : Double.MAX_VALUE; // Avoid division by zero
					}))
					.collect(Collectors.toList());
    	/*
    	for(MobilePlans m :sortedPlans) {
    		System.out.println(m.provider+" "+m.plan.get("Plan Name")+m.plan.get("Plan Cost")+m.plan.get("Data"));
    	}*/
			MobilePlans plan1 = sortedPlans.get(0);
			MobilePlans plan2 = sortedPlans.get(1);
			MobilePlans plan3 = sortedPlans.get(2);
			MobilePlans plan4 = sortedPlans.get(3);
			 planDetail1 = "Plan Provider :" + plan1.provider + "\n" + "Plan Name: " + plan1.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan1.plan.get("Plan Cost") + "\n" + "URL: " + plan1.link;
			 planDetail2 = "Plan Provider :" + plan2.provider + "\n" + "Plan Name: " + plan2.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan2.plan.get("Plan Cost") + "\n" + "URL: " + plan2.link;
			 planDetail3 = "Plan Provider :" + plan3.provider + "\n" + "Plan Name: " + plan3.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan3.plan.get("Plan Cost") + "\n" + "URL: " + plan3.link;
			 planDetail4 = "Plan Provider :" + plan4.provider + "\n" + "Plan Name: " + plan4.plan.get("Plan Name") + "\n" + "Plan Cost: " + plan4.plan.get("Plan Cost") + "\n" + "URL: " + plan4.link;
		}
		catch (IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
    	return planDetail1+"\n\n"+planDetail2 +"\n\n"+planDetail3 +"\n\n"+planDetail4;
    }
    
    public static void main(String args[]) {
    	System.out.println(comparePlansByCost());
    	System.out.println(comparePlansByData());
    	
    }
}
