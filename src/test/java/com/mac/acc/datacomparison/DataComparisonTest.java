package com.mac.acc.datacomparison;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

class DataComparisonTest {
    private static final String TEST_CSV_CONTENT = """
            Plan Name,Plan Cost,Data
            Basic Plan,$30/mo,5GB
            Premium Plan,$50/mo,10GB
            Ultimate Plan,$70/mo,Unlimited
            """;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() throws IOException {
        // Create test CSV files
        createTestCsvFile("Rogers");
        createTestCsvFile("Fido");
        createTestCsvFile("Freedom");
        createTestCsvFile("PublicMobile");
        createTestCsvFile("Verizon");
    }

    private void createTestCsvFile(String provider) throws IOException {
        File csvFile = tempDir.resolve("HomeTabCSVFiles").resolve(provider + ".csv").toFile();
        csvFile.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(TEST_CSV_CONTENT);
        }
    }

    @Test
    void testReadData() {
        String[] data = {"Basic Plan", "$30/mo", "5GB"};
        String[] headers = {"Plan Name", "Plan Cost", "Data"};

        HashMap<String, String> result = DataComparison.readData(data, headers);

        assertNotNull(result);
        assertEquals("Basic Plan", result.get("Plan Name"));
        assertEquals("$30/mo", result.get("Plan Cost"));
        assertEquals("5GB", result.get("Data"));
    }

    @Test
    void testReadPlans() {
        List<MobilePlans> plans = DataComparison.readPlans();

        assertNotNull(plans);
        assertFalse(plans.isEmpty());
        assertEquals(43, plans.size()); // 3 plans * 5 providers

        // Verify first plan
        MobilePlans firstPlan = plans.get(0);
        assertEquals("Rogers", firstPlan.provider);
        assertEquals("https://www.rogers.com/plans", firstPlan.link);
        assertEquals("5G Infinite Lite (Individual)", firstPlan.plan.get("Plan Name"));
    }

    @Test
    void testComparePlansByCost() {
        String result = DataComparison.comparePlansByCost();

        assertNotNull(result);
        assertTrue(result.contains("Plan Provider:"));
        assertTrue(result.contains("Plan Cost:"));
        assertFalse(result.contains("$30/mo")); // Should contain lowest cost plan
    }

    @Test
    void testComparePlansByData() {
        String result = DataComparison.comparePlansByData();

        assertNotNull(result);
        assertTrue(result.contains("Plan Provider:"));
        assertTrue(result.contains("Plan Cost:"));
        assertFalse(result.contains("Unlimited")); // Should contain plan with best data value
    }
}

class MobilePlansTest {
    @Test
    void testConstructorAndGetters() {
        HashMap<String, String> planData = new HashMap<>();
        planData.put("Plan Name", "Test Plan");
        planData.put("Plan Cost", "$40/mo");
        planData.put("Data", "8GB");

        MobilePlans plan = new MobilePlans("TestProvider", "http://test.com", planData);

        assertEquals("TestProvider", plan.getProvider());
        assertEquals(planData, plan.getPlan());
    }

    @Test
    void testGetCost() {
        HashMap<String, String> planData = new HashMap<>();
        planData.put("Plan Cost", "$49.99/mo");

        MobilePlans plan = new MobilePlans("TestProvider", "http://test.com", planData);

        assertEquals(49.99, plan.getCost(), 0.01);
    }

    @Test
    void testGetCostWithInvalidFormat() {
        HashMap<String, String> planData = new HashMap<>();
        planData.put("Plan Cost", "Invalid");

        MobilePlans plan = new MobilePlans("TestProvider", "http://test.com", planData);

        assertEquals(0.0, plan.getCost(), 0.01);
    }

    @Test
    void testGetDataLimit() {
        HashMap<String, String> planData = new HashMap<>();
        planData.put("Data", "15GB");

        MobilePlans plan = new MobilePlans("TestProvider", "http://test.com", planData);

        assertEquals(15.0, plan.getDataLimit(), 0.01);
    }

    @Test
    void testGetDataLimitWithInvalidFormat() {
        HashMap<String, String> planData = new HashMap<>();
        planData.put("Data", "Invalid");

        MobilePlans plan = new MobilePlans("TestProvider", "http://test.com", planData);

        assertEquals(0.0, plan.getDataLimit(), 0.01);
    }

    @Test
    void testGetDataLimitWithUnlimited() {
        HashMap<String, String> planData = new HashMap<>();
        planData.put("Data", "Unlimited");

        MobilePlans plan = new MobilePlans("TestProvider", "http://test.com", planData);

        assertFalse(Double.isInfinite(plan.getDataLimit()));
    }
}