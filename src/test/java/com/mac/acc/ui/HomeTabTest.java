package com.mac.acc.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class HomeTabTest {
    private HomeTab homeTab;

    @BeforeEach
    void setUp() {
        homeTab = new HomeTab();
    }

    @Test
    void testHomeTabInitialization() {
        assertNotNull(homeTab);
        Component[] components = homeTab.getComponents();
        assertTrue(components.length > 0);

        // Verify label
        Optional<JLabel> label = Arrays.stream(components)
                .filter(c -> c instanceof JLabel)
                .map(c -> (JLabel)c)
                .findFirst();
        assertTrue(label.isPresent());
        assertEquals("Get the best plan based on data or price", label.get().getText());
    }

    @Test
    void testButtonActions() {
        Optional<JPanel> buttonPanel = Arrays.stream(homeTab.getComponents())
                .filter(c -> c instanceof JPanel)
                .map(c -> (JPanel)c)
                .findFirst();
        assertTrue(buttonPanel.isPresent());

        Component[] buttons = buttonPanel.get().getComponents();
        assertEquals(2, buttons.length);

        JButton dataButton = (JButton)buttons[0];
        JButton priceButton = (JButton)buttons[1];

        // Simulate button clicks
        dataButton.getActionListeners()[0].actionPerformed(
                new ActionEvent(dataButton, ActionEvent.ACTION_PERFORMED, ""));
        priceButton.getActionListeners()[0].actionPerformed(
                new ActionEvent(priceButton, ActionEvent.ACTION_PERFORMED, ""));
    }
}

class RecommendationTabTest {
    private RecommendationTab recommendationTab;

    @BeforeEach
    void setUp() {
        recommendationTab = new RecommendationTab();
    }

    @Test
    void testTabInitialization() {
        assertNotNull(recommendationTab);
        assertTrue(recommendationTab.getComponents().length > 0);
    }

    @Test
    void testValidateInput() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // Test method through reflection since it's private
        Method validateInput = RecommendationTab.class.getDeclaredMethod(
                "validateInput", double.class, double.class, String.class);
        validateInput.setAccessible(true);

        assertTrue((Boolean)validateInput.invoke(recommendationTab, 50.0, 10.0, "1,2,3"));
        assertFalse((Boolean)validateInput.invoke(recommendationTab, -1.0, 10.0, "1,2,3"));
        assertFalse((Boolean)validateInput.invoke(recommendationTab, 50.0, -1.0, "1,2,3"));
        assertFalse((Boolean)validateInput.invoke(recommendationTab, 50.0, 10.0, "1,2,6"));
    }

    @Test
    void testParseFeatures() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method parseFeatures = RecommendationTab.class.getDeclaredMethod(
                "parseFeatures", String.class);
        parseFeatures.setAccessible(true);

        List<String> features = (List<String>)parseFeatures.invoke(
                recommendationTab, "1,2,3");

        assertEquals(3, features.size());
        assertTrue(features.contains("Unlimited Talk Time"));
        assertTrue(features.contains("Unlimited Text"));
        assertTrue(features.contains("International Calls"));
    }
}

class SearchBarUITest {
    @Test
    void testCreatePanel() {
        JPanel panel = SearchBarUI.createPanel();
        assertNotNull(panel);

        // Verify search components
        Optional<JTextField> searchField = findComponentByType(panel, JTextField.class);
        Optional<JButton> searchButton = findComponentByType(panel, JButton.class);

        assertTrue(searchField.isPresent());
        assertTrue(searchButton.isPresent());
    }

    @Test
    void testSearchFunctionality() {
        JPanel panel = SearchBarUI.createPanel();
        Optional<JTextField> searchField = findComponentByType(panel, JTextField.class);
        Optional<JButton> searchButton = findComponentByType(panel, JButton.class);

        searchField.get().setText("test");
        searchButton.get().getActionListeners()[0].actionPerformed(
                new ActionEvent(searchButton.get(), ActionEvent.ACTION_PERFORMED, ""));
    }

    private <T extends Component> Optional<T> findComponentByType(Container container, Class<T> type) {
        for (Component c : container.getComponents()) {
            if (type.isInstance(c)) {
                return Optional.of(type.cast(c));
            }
            if (c instanceof Container) {
                Optional<T> result = findComponentByType((Container)c, type);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }
}

class SupportTabTest {
    private SupportTab supportTab;

    @BeforeEach
    void setUp() {
        supportTab = new SupportTab();
    }

    @Test
    void testEmailValidation() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method isValidEmail = SupportTab.class.getDeclaredMethod(
                "isValidEmailAddress", String.class);
        isValidEmail.setAccessible(true);

        assertTrue((Boolean)isValidEmail.invoke(supportTab, "test@example.com"));
        assertFalse((Boolean)isValidEmail.invoke(supportTab, "invalid-email"));
        assertFalse((Boolean)isValidEmail.invoke(supportTab, "test@.com"));
    }

    @Test
    void testPhoneValidation() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method isValidPhone = SupportTab.class.getDeclaredMethod(
                "isValidPhoneNumber", String.class);
        isValidPhone.setAccessible(true);

        assertTrue((Boolean)isValidPhone.invoke(supportTab, "1234567890"));
        assertTrue((Boolean)isValidPhone.invoke(supportTab, "(123) 456-7890"));
        assertFalse((Boolean)isValidPhone.invoke(supportTab, "123-abc-7890"));
    }

    @Test
    void testWriteToFile() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method writeToFile = SupportTab.class.getDeclaredMethod(
                "writeToFile", String.class, String.class, String.class);
        writeToFile.setAccessible(true);

        writeToFile.invoke(supportTab, "test@example.com", "1234567890", "Test complaint");

        File complaintFile = new File("complaints.txt");
        assertTrue(complaintFile.exists());

        List<String> lines = Files.readAllLines(complaintFile.toPath());
        assertTrue(lines.contains("Email: test@example.com"));
        assertTrue(lines.contains("Phone: 1234567890"));
        assertTrue(lines.contains("Complaint: Test complaint"));

        complaintFile.delete();
    }
}