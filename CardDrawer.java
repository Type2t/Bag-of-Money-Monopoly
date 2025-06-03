import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CardDrawer {
    private JButton drawCardButton;
    private JLabel cardLabel;
    private String[] chanceCards = {
            "Advance to GO",
            "Go directly to Jail",
            "Bank pays you $50",
            "Your building loan matures, collect $150",
            "Pay poor tax of $15"
    };

    private String[] communityChestCards = {
            "Doctor's fee: Pay $50",
            "From sale of stock you get $50",
            "Receive $25 consultancy fee",
            "You inherit $100",
            "Get out of Jail Free card"
    };

    public CardDrawer() {
        drawCardButton = new JButton("Draw Card");
        drawCardButton.setPreferredSize(new Dimension(150, 50));
        cardLabel = new JLabel("No card drawn yet", SwingConstants.CENTER);
        cardLabel.setFont(new Font("Arial", Font.BOLD, 16));

        drawCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                boolean isChance = rand.nextBoolean();
                String card;
                if (isChance) {
                    card = chanceCards[rand.nextInt(chanceCards.length)];
                    cardLabel.setText("Chance: " + card);
                } else {
                    card = communityChestCards[rand.nextInt(communityChestCards.length)];
                    cardLabel.setText("Community Chest: " + card);
                }
            }
        });
    }

    public JPanel getCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.add(drawCardButton, BorderLayout.NORTH);
        cardPanel.add(cardLabel, BorderLayout.CENTER);
        return cardPanel;
    }
}