import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class Dice {
    @SuppressWarnings("unused")
    private JButton rollButton;
    private JLabel resultLabel;
    private int diceResult;

    public Dice(JButton rollButton, JLabel resultLabel) {
        this.rollButton = rollButton;
        this.resultLabel = resultLabel;

        // Add action listener to roll the dice when the button is clicked
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollDiceWithAnimation();  // Calling the updated method
            }
        });
    }

    // Method to handle dice roll with animation and sound
    public void rollDiceWithAnimation() {
        Random rand = new Random();
        diceResult = (rand.nextInt(6) + 1 + (rand.nextInt(6) + 1));  // Rolls 2 dice to get a number between 2 and 12
        resultLabel.setText("You rolled: " + diceResult);
        
        // Optional: Add animation and sound logic here
        System.out.println("Dice Rolled: " + diceResult);  // Placeholder for actual animation/sound
        
        // Optionally, add animation or sound here
        // For example, using Java's javax.sound.sampled package for sound
    }

    // Getter for the dice result
    public int getDiceResult() {
        return diceResult;
    }
}