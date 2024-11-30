package com.mac.acc.search;

import com.mac.acc.search.engine.FieldCondition;
import com.mac.acc.search.engine.SearchEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Weiming Zheng
 * @since 2024-11-30
 */
class MobileDataPlanSearchEngineTest {
    private SearchEngine searchEngine;
    private List<Document> testDocuments;

    @BeforeEach
    void setUp() {
        // Initialize test documents
        testDocuments = Arrays.asList(
                new Document(Path.of("docs/public-mobile.html"),
                        "https://www.publicmobile.ca/en/on/plans"),
                new Document(Path.of("docs/fido-byod.html"),
//                        "https://www.fido.ca/phones/bring-your-own-device?flowType=byod"),
                        "https://www.fido.ca/phones/bring-your-own-device?icid=F_WIR_CNV_GRM6LG&flowType=byod&step=2&data=sku_plan_FPMM012JE_FPMM012JE&sku=BYOD&tier=NOTERM&talk=sku_plan_FPMM012JE_FPMM012JE"),
                new Document(Path.of("docs/rogers-plans.html"),
                        "https://www.rogers.com/plans"),
                new Document(Path.of("docs/rogers-family.html"),
                        "https://www.rogers.com/mobility/family-phones"),
                new Document(Path.of("docs/verizon-unlimited.html"),
                        "https://www.verizon.com/plans/unlimited/"),
                new Document(Path.of("docs/verizon-prepaid.html"),
                        "https://www.verizon.com/plans/prepaid/")
        );

        searchEngine = SearchEngineFactory.getSearchEngine(testDocuments);
    }

    @Test
    @DisplayName("Test single field")
    void testSingleFieldCondition() {
        // Create a field with loose condition looking for "talk plan"
        Field field = new Field("talk plan", FieldCondition.LOOSE, false);

        List<Map.Entry<String, Integer>> results = searchEngine.search(List.of(field));

        assertFalse(results.isEmpty(), "Should return matching documents");
        assertTrue(results.stream()
                        .anyMatch(entry -> entry.getKey().contains("verizon.com/plans/unlimited")),
                "Verizon unlimited plans page should be in results");
        assertTrue(results.stream()
                        .anyMatch(entry -> entry.getKey().contains("rogers.com/mobility/family")),
                "Rogers family plans page should be in results");
    }

    @Test
    @DisplayName("Test empty field")
    void testEmptyField() {
        // Create a field with loose condition looking for "talk plan"
        Field field = new Field("", FieldCondition.LOOSE, false);

        List<Map.Entry<String, Integer>> results = searchEngine.search(List.of(field));

        assertTrue(results.isEmpty(), "Should return zero matching documents");
    }

    @Test
    @DisplayName("Test multiple fields with mixed conditions")
    void testMixedFieldConditions() {
        // Create two fields: one tight for "prepaid" and one loose for "unlimited data"
        Field field1 = new Field("rogers", FieldCondition.TIGHT, false);
        Field field2 = new Field("fido", FieldCondition.TIGHT, false);
        Field field3 = new Field("verizon", FieldCondition.LOOSE, false);

        List<Map.Entry<String, Integer>> results = searchEngine.search(Arrays.asList(field1, field2, field3));

        assertFalse(results.isEmpty(), "Should return matching documents");
        assertTrue(results.stream()
                        .anyMatch(entry -> entry.getKey().contains("fido")),
                "Should find documents with 'prepaid'");
    }

    @Test
    @DisplayName("Test case sensitivity in search fields")
    void testCaseSensitiveSearch() {
        // Create two identical fields with different case sensitivity
        Field caseSensitiveField = new Field("PREPAID", FieldCondition.TIGHT, true);
        Field caseInsensitiveField = new Field("PREPAID", FieldCondition.TIGHT, false);

        List<Map.Entry<String, Integer>> sensResults = searchEngine.search(List.of(caseSensitiveField));
        List<Map.Entry<String, Integer>> insensResults = searchEngine.search(List.of(caseInsensitiveField));

        for (int i = 0; i < sensResults.size(); ++ i) {
            assertNotEquals(sensResults.get(i), insensResults.get(i),
                    "Case sensitive and insensitive searches should return different results");
        }
    }

    @Test
    @DisplayName("Test empty results when no matches found")
    void testNoMatchesFound() {
        // Create a field with a word combination that shouldn't exist in any document
        Field field = new Field("nonexistentword", FieldCondition.TIGHT, false);

        List<Map.Entry<String, Integer>> results = searchEngine.search(List.of(field));

        assertTrue(results.stream().allMatch(entry -> entry.getValue() == 0),
                "All documents should have zero relevance for non-existent words");
    }

    @Test
    @DisplayName("Test result ordering by relevance score")
    void testResultOrdering() {
        // Create a field that should match multiple documents
        Field field = new Field("plan data", FieldCondition.LOOSE, false);

        List<Map.Entry<String, Integer>> results = searchEngine.search(List.of(field));

        assertFalse(results.isEmpty(), "Should return matching documents");

        // Verify results are in descending order by relevance score
        for (int i = 1; i < results.size(); ++ i) {
            assertTrue(results.get(i - 1).getValue() >= results.get(i).getValue(),
                    "Results should be in descending order by relevance score");
        }
    }
}