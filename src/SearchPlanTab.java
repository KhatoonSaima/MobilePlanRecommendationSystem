import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPlanTab extends JPanel {

    private JLabel resultLabel;

    public SearchPlanTab() {
        setLayout(new FlowLayout());

        // Dropdown for mobile carrier selection
        JComboBox<String> selectDropdown = new JComboBox<>(
                new String[]{"Verizon", "Fido", "Freedom", "Simplemobile", "Rogers"}
        );

        // Create a button for search action
        JButton searchButton = new JButton("Search");

        // Label to display the selected carrier
        resultLabel = new JLabel("");
        //add(searchLabel);
        //add(searchField);

        // Add the dropdown, button and label to the panel
        add(selectDropdown);
        add(searchButton);
        add(resultLabel);

        // action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method when the button is clicked
                getPlansSelectedCarrier(selectDropdown);
            }
        });
    }

    // Function to display all the plan for selected carrier
    private void getPlansSelectedCarrier(JComboBox<String> dropdown) {
       // code to get all the plans of selected carrier and display it
    }
}
