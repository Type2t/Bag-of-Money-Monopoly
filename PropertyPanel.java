import java.awt.*;
import javax.swing.*;

public class PropertyPanel {
    private final BoardProperty property;
    private final JPanel panel;

    public PropertyPanel(BoardProperty property) {
        this.property = property;
        this.panel = new JPanel();
        initializePanel();
    }

    // Initializes the property panel
    private void initializePanel() {
        panel.setPreferredSize(new Dimension(15, 25));
        panel.setBackground(getPropertyColor(property.getProperColor()));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add click listener to show property details
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPropertyDialog();
            }
        });
    }

    // Displays a dialog with property details and options
    private void showPropertyDialog() {
        String message = String.format(
            "Name: %s\nOwner: %s\nHouses: %d",
            property.getProperName(),
            (property.getOwner() == null ? "Bank" : property.getOwner().getName()),
            property.getHouseCount()
        );

        // If the current player is the owner, show additional options
        if (property.getOwner() == GameCore.playerList.get(GameCore.getCurrentPlayer())) {
            String[] options = {"Mortgage", "Repay Mortgage", "Buy House", "Sell House", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                null,
                message,
                "Property Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[4]
            );

            handleOwnerChoice(choice);
        } else {
            JOptionPane.showMessageDialog(null, message, "Property Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Handles the owner's choice from the dialog
    private void handleOwnerChoice(int choice) {
        switch (choice) {
            case 0 -> property.mortgage();
            case 1 -> property.repayMortgage();
            case 2 -> property.addHouse();
            case 3 -> property.removeHouse();
            default -> {} // Do nothing for "Cancel"
        }
    }

    // Returns the panel for this property
    public JPanel getPanel() {
        return panel;
    }

    // Helper method to get the color for the property
    private Color getPropertyColor(BoardProperty.color propertyColor) {
        return switch (propertyColor) {
            case BLUE -> new Color(32, 32, 204);
            case GREEN -> new Color(51, 128, 51);
            case YELLOW -> new Color(255, 255, 0);
            case ORANGE -> new Color(255, 128, 0);
            case RED -> new Color(255, 0, 0);
            case PINK -> new Color(255, 0, 255);
            case BABY_BLUE -> new Color(194, 229, 252);
            case BROWN -> new Color(128, 51, 153);
            default -> new Color(191, 219, 174);
        };
    }
}