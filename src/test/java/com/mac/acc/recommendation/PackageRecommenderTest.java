package com.mac.acc.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class PackageTest {
    @Test
    void testPackageConstructorAndGetters() {
        List<String> features = Arrays.asList("Unlimited Talk", "5G");
        Package pkg = new Package("Basic", "Telco", 10.0, 29.99, features);

        assertEquals("Basic", pkg.getName());
        assertEquals("Telco", pkg.getBrand());
        assertEquals(10.0, pkg.getDataLimit());
        assertEquals(29.99, pkg.getPrice());
        assertEquals(features, pkg.getFeatures());
    }

    @Test
    void testPackageWithNullFeatures() {
        Package pkg = new Package("Basic", "Telco", 10.0, 29.99, null);
        assertNotNull(pkg.getFeatures());
        assertTrue(pkg.getFeatures().isEmpty());
    }
}

class PackageRecommenderTest {
    private PackageRecommender recommender;

    @BeforeEach
    void setUp() {
        recommender = new PackageRecommender();
        // Add test packages
        recommender.addPackage(new Package("Basic", "Telco1", 5.0, 30.0,
                Arrays.asList("Unlimited Talk", "Text")));
        recommender.addPackage(new Package("Premium", "Telco1", 10.0, 50.0,
                Arrays.asList("Unlimited Talk", "Text", "International")));
        recommender.addPackage(new Package("Ultimate", "Telco1", Double.POSITIVE_INFINITY, 70.0,
                Arrays.asList("Unlimited Talk", "Text", "International", "Roaming")));
    }

    @Test
    void testRecommendTopK() {
        List<Package> recommendations = recommender.recommendTopK(8.0, 60.0,
                Arrays.asList("International"));

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        assertTrue(recommendations.size() <= 3);

        // First recommendation should be Premium plan as it matches criteria best
        assertEquals("Premium", recommendations.get(0).getName());
    }

    @Test
    void testRecommendWithUnlimitedData() {
        List<Package> recommendations = recommender.recommendTopK(20.0, 100.0,
                Arrays.asList("International", "Roaming"));

        assertFalse(recommendations.isEmpty());
        assertEquals("Ultimate", recommendations.get(0).getName());
    }

    @Test
    void testRecommendWithBudgetConstraint() {
        List<Package> recommendations = recommender.recommendTopK(5.0, 40.0, null);

        assertFalse(recommendations.isEmpty());
        for (Package pkg : recommendations) {
            assertTrue(pkg.getPrice() <= 40.0);
        }
    }

    @Test
    void testGetPackageByName() {
        Package pkg = recommender.getPackageByName("Basic");
        assertNotNull(pkg);
        assertEquals("Basic", pkg.getName());
    }

    @Test
    void testGetNonexistentPackage() {
        Package pkg = recommender.getPackageByName("Nonexistent");
        assertNull(pkg);
    }
}

class ScoredPackageTest {
    @Test
    void testConstructorAndGetters() {
        Package pkg = new Package("Test", "Brand", 5.0, 30.0, null);
        ScoredPackage scoredPkg = new ScoredPackage(8.5, pkg);

        assertEquals(8.5, scoredPkg.getScore());
        assertEquals(pkg, scoredPkg.getPackage());
    }

    @Test
    void testCompareTo() {
        Package pkg1 = new Package("Test1", "Brand", 5.0, 30.0, null);
        Package pkg2 = new Package("Test2", "Brand", 5.0, 30.0, null);

        ScoredPackage scoredPkg1 = new ScoredPackage(8.5, pkg1);
        ScoredPackage scoredPkg2 = new ScoredPackage(9.0, pkg2);

        assertTrue(scoredPkg1.compareTo(scoredPkg2) < 0);
        assertTrue(scoredPkg2.compareTo(scoredPkg1) > 0);
        assertEquals(0, scoredPkg1.compareTo(new ScoredPackage(8.5, pkg2)));
    }
}

class UserPreferencesTest {
    @Test
    void testConstructorAndGetters() {
        List<String> preferences = Arrays.asList("Unlimited", "International");
        UserPreferences prefs = new UserPreferences(10.0, 50.0, preferences);

        assertEquals(10.0, prefs.getDesiredData());
        assertEquals(50.0, prefs.getMaxPrice());
        assertEquals(preferences, prefs.getPreferences());
    }
}