package com.mac.acc.search;
import com.mac.acc.ui.SearchBarUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class SearchBarUITest {

    @Test
    void testSearchBarUIComponents() {
        // Create the main panel
        JPanel panel = SearchBarUI.createPanel();

        // Assert that the panel is not null
        assertNotNull(panel, "Panel should not be null");

        // Assert that the panel has two components (top and bottom panels)
        assertEquals(2, panel.getComponentCount(), "Panel should have two child panels");

        // Check top and bottom panel components
        JPanel topPanel = (JPanel) panel.getComponent(0);
        JPanel bottomPanel = (JPanel) panel.getComponent(1);

        assertNotNull(topPanel, "Top panel should not be null");
        assertNotNull(bottomPanel, "Bottom panel should not be null");
    }

    @Test
    void testSearchFieldAndButton() {
        JPanel panel = SearchBarUI.createPanel();

        // Get the top panel and search bar components
        JPanel topPanel = (JPanel) panel.getComponent(0);
        JPanel searchBarPanel = (JPanel) topPanel.getComponent(0);

        JPanel inputPanel = (JPanel) searchBarPanel.getComponent(0);
        JTextField searchField = (JTextField) inputPanel.getComponent(1);
        JButton searchButton = (JButton) inputPanel.getComponent(2);

        // Assert that the search field and button exist
        assertNotNull(searchField, "Search field should not be null");
        assertNotNull(searchButton, "Search button should not be null");

        // Simulate input in the search field
        searchField.setText("test query");
        assertEquals("test query", searchField.getText(), "Search field should contain the entered text");

        // Simulate clicking the search button
        searchButton.doClick();
        // No exceptions should occur; the method should execute successfully
    }

    @Test
    void testSuggestionsPanelVisibility() {
        JPanel panel = SearchBarUI.createPanel();

        // Get the top panel and search bar components
        JPanel topPanel = (JPanel) panel.getComponent(0);
        JPanel searchBarPanel = (JPanel) topPanel.getComponent(0);

        JPanel suggestionsPanel = (JPanel) searchBarPanel.getComponent(1);

        // Initially, the suggestions panel should be hidden
        assertFalse(suggestionsPanel.isVisible(), "Suggestions panel should initially be hidden");

        // Simulate showing suggestions
        suggestionsPanel.setVisible(true);
        assertTrue(suggestionsPanel.isVisible(), "Suggestions panel should be visible after being shown");
    }

    @Test
    void testFrequencyPanelUpdates() {
        JPanel panel = SearchBarUI.createPanel();

        // Get the bottom panel and frequency panel components
        JPanel bottomPanel = (JPanel) panel.getComponent(1);
        JPanel frequencyContainer = (JPanel) bottomPanel.getComponent(0);

        JScrollPane frequencyScrollPane = (JScrollPane) frequencyContainer.getComponent(0);
        JPanel frequencyPanel = (JPanel) frequencyScrollPane.getViewport().getView();

        // Initially, the frequency panel should be empty
        assertEquals(0, frequencyPanel.getComponentCount(), "Frequency panel should initially be empty");

        // Simulate adding a frequency entry
        JLabel mockFrequencyEntry = new JLabel("test query: 1");
        frequencyPanel.add(mockFrequencyEntry);

        // Assert that the frequency panel now contains one entry
        assertEquals(1, frequencyPanel.getComponentCount(), "Frequency panel should contain one entry after update");
    }
}