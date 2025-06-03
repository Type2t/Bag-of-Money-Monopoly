import java.awt.*;
import javax.swing.*;

public class MonopolyBoard {
    private final JFrame frame; // Main frame for the Monopoly board
    private final JPanel leftPanel, rightPanel; // Panels for the board and info sections
    private final Image backgroundImage; // Background image for the board

    public MonopolyBoard() {
        // Initialize the main frame
        frame = new JFrame("Monopoly Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1125, 925);
        frame.setLayout(new GridBagLayout());

        // Load the background image
        backgroundImage = new ImageIcon("images/board_background2.jpeg").getImage();
        if (backgroundImage == null) System.out.println("Background image not found!");

        // Create the left (board) and right (info) panels
        leftPanel = createPanel(new BackgroundPanel(backgroundImage), new Dimension(925, 925));
        rightPanel = createPanel(new JPanel(), new Dimension(200, 925), Color.WHITE);

        // Initialize the board and info panel
        initializeBoard(leftPanel);
        initializeInfoPanel(rightPanel);

        // Add panels to the frame
        addPanelToFrame(leftPanel, 0, 0, 1.0, 1.0);
        addPanelToFrame(rightPanel, 1, 0, 0.2, 1.0);

        // Finalize frame setup
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // Creates a panel with default background color
    private JPanel createPanel(JPanel panel, Dimension size) {
        return createPanel(panel, size, new Color(191, 219, 174));
    }

    // Creates a panel with specified size and background color
    private JPanel createPanel(JPanel panel, Dimension size, Color bgColor) {
        panel.setLayout(new GridBagLayout());
        panel.setMinimumSize(size);
        panel.setMaximumSize(size);
        panel.setPreferredSize(size);
        panel.setBackground(bgColor);
        return panel;
    }

    // Adds a panel to the frame with specified layout constraints
    private void addPanelToFrame(JPanel panel, int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = gridx;
        cons.gridy = gridy;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = weightx;
        cons.weighty = weighty;
        frame.add(panel, cons);
    }

    // Initializes the board by creating and adding tiles
    private void initializeBoard(JPanel boardPan) {
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                JPanel tileLabel = createTile(x, y);
                addTileToBoard(boardPan, tileLabel, x, y);
            }
        }
    }

    // Creates a tile for the board based on its position
    private JPanel createTile(int x, int y) {
        JPanel tileLabel = createPanel(new JPanel(), new Dimension(75 + ((x == 0 || x == 10) ? 50 : 0), 75 + ((y == 0 || y == 10) ? 50 : 0)));
        if ((x > 0 && x < 10) && (y > 0 && y < 10)) tileLabel.setOpaque(false); // Center tiles are transparent
        else tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Border for edge tiles

        // Add property or special tile based on position
        if ((x < 1 || x > 9) || (y < 1 || y > 9)) {
            BoardProperty property = GameCore.bProperties.get(getPositionFromTile(x, y));
            if (property.getProperType() == BoardProperty.propertyType.REGULAR) {
                addRegularProperty(tileLabel, property, x, y);
            } else {
                addSpecialTile(tileLabel, property, x, y);
            }
        }
        return tileLabel;
    }

    // Adds a regular property tile to the board
    private void addRegularProperty(JPanel tileLabel, BoardProperty property, int x, int y) {
        // First panel for property name and cost
        JPanel firstPanel = createPanel(new JPanel(), getPropertyPanelSize(x, y));
        firstPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        firstPanel.add(new JLabel("<html>" + property.getProperName().replaceAll(" ", "<br>") + "<br>$" + property.getProperCost() + "</html>"));
    
        // Second panel for property color
        JPanel secondPanel = createPanel(new JPanel(), getSecondPanelSize(x, y), getPropertyColor(property.getProperColor()));
        secondPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
        // Add panels to the tile
        addToTile(tileLabel, firstPanel, (x == 10) ? 1 : 0, (y == 10) ? 1 : 0);
        addToTile(tileLabel, secondPanel, (x == 0) ? 1 : 0, (y == 0) ? 1 : 0);
    }

    // Determines the size of a property panel based on its position
    private Dimension getPropertyPanelSize(int x, int y) {
        if (x == 0 || x == 10) {
            return new Dimension(100, 75); // First panel: 75px wide, 100px high
        } else if (y == 0 || y == 10) {
            return new Dimension(75, 100); // First panel: 100px wide, 75px high
        }
        return new Dimension(75, 75); // Default size
    }

    private Dimension getSecondPanelSize(int x, int y) {
        if (x == 0 || x == 10) {
            return new Dimension(25, 75); // Second panel: 75px wide, 25px high
        } else if (y == 0 || y == 10) {
            return new Dimension(75, 25); // Second panel: 25px wide, 75px high
        }
        return new Dimension(75, 75); // Default size
    }

    // Adds a special tile (e.g., GO, Jail) to the board
    private void addSpecialTile(JPanel tileLabel, BoardProperty property, int x, int y) {
        // Determine the size of the tile
        int width = 75 + ((x == 0 || x == 10) ? 50 : 0);
        int height = 75 + ((y == 0 || y == 10) ? 50 : 0);

        // Create the panel for the tile
        JPanel tilePan = createPanel(new JPanel(), new Dimension(width, height));
        tilePan.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Black border for the tile panel

        // Retrieve and scale the icon to fill the panel
        ImageIcon icon = getTileImage(property.getProperType(), width, height);
        if (icon != null) {
            JLabel tileLab = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            tilePan.add(tileLab);
        }

        // Add the tile panel to the main tile label
        addToTile(tileLabel, tilePan, 0, 0);

        // Ensure the main tile label also has a black border
        tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    // Adds a panel to a tile with specified layout constraints
    private void addToTile(JPanel tileLabel, JPanel panel, int gridx, int gridy) {
        GridBagConstraints tileCon = new GridBagConstraints();
        tileCon.gridx = gridx;
        tileCon.gridy = gridy;
        tileLabel.add(panel, tileCon);
    }

    // Adds a tile to the board with specified layout constraints
    private void addTileToBoard(JPanel boardPan, JPanel tileLabel, int x, int y) {
        GridBagConstraints boardGBC = new GridBagConstraints();
        boardGBC.gridx = x;
        boardGBC.gridy = y;

        // Add the tile to the board
        boardPan.add(tileLabel, boardGBC);

        // Overlay player tokens on the tile
        overlayPlayerTokens(tileLabel, x, y);
    }

    // Overlays player tokens on a tile based on their positions
    private void overlayPlayerTokens(JPanel tileLabel, int x, int y) {
        int tokenSize = 25; // Size of each token
        int spacing = 10;   // Spacing between tokens
    
        // Use the preferred width of the tileLabel to calculate maxTokensPerRow
        int tileWidth = tileLabel.getPreferredSize().width;
        int maxTokensPerRow = (tileWidth - spacing) / (tokenSize + spacing);
        if (maxTokensPerRow <= 0) maxTokensPerRow = 1; // Ensure at least one token per row
    
        int currentTokenIndex = 0; // Tracks the index of the token being placed
    
        for (Player player : GameCore.playerList) {
            int playerPosition = player.getPosition();
            int tilePosition = getPositionFromTile(x, y);
    
            if (playerPosition == tilePosition) {
                // Get the token image for the player
                ImageIcon tokenIcon = getTokenImage(player.getToken());
                if (tokenIcon != null) {
                    JLabel tokenLabel = new JLabel(tokenIcon);
                    tokenLabel.setOpaque(false); // Ensure transparency
    
                    // Calculate the position of the token
                    int row = currentTokenIndex / maxTokensPerRow; // Determine the row
                    int col = currentTokenIndex % maxTokensPerRow; // Determine the column
                    int xOffset = spacing + col * (tokenSize + spacing);
                    int yOffset = spacing + row * (tokenSize + spacing);
    
                    tokenLabel.setBounds(xOffset, yOffset, tokenSize, tokenSize); // Set position and size
    
                    // Add the token label to the tile
                    tileLabel.add(tokenLabel, 0); // Add to the top layer
                    tileLabel.setComponentZOrder(tokenLabel, 0); // Ensure token is on top
                    tileLabel.revalidate();
                    tileLabel.repaint();
    
                    currentTokenIndex++; // Move to the next token position
                }
            }
        }
    }

    // Initializes the info panel with sections
    private void initializeInfoPanel(JPanel boardPan) {
        // Add a panel for the current player's information
        JPanel currentPlayerPanel = createPanel(new JPanel(), new Dimension(200, 200));
        currentPlayerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
        // Create a JLabel with word wrapping to display the current player's information
        JLabel playerInfoLabel = new JLabel();
        playerInfoLabel.setVerticalAlignment(SwingConstants.TOP); // Align text to the top
        playerInfoLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left
        playerInfoLabel.setText(getCurrentPlayerInfo()); // Set the initial text
        playerInfoLabel.setPreferredSize(new Dimension(190, 190)); // Ensure it fits within the panel
    
        // Add the label to the panel
        currentPlayerPanel.add(playerInfoLabel);
    
        // Add the current player panel to the board
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        boardPan.add(currentPlayerPanel, gbc);
    
        // Add the other info panel sections
        addInfoPanelSection(boardPan, 1, new Dimension(200, 525));
    
        // Add a panel for the dice roll
        JPanel dicePanel = createPanel(new JPanel(), new Dimension(200, 200));
        dicePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
        // Create a JLabel to display the dice result
        JLabel diceResultLabel = new JLabel("Dice Result: ");
        diceResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        diceResultLabel.setPreferredSize(new Dimension(190, 50));
    
        // Create a button to roll the dice
        JButton rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.setPreferredSize(new Dimension(100, 50));
    
        // Add action listener to the button
        rollDiceButton.addActionListener(e -> {
            // Roll the dice
            Dice dice = new Dice(rollDiceButton, diceResultLabel);
            dice.rollDiceWithAnimation();
        
            // Get the current player
            Player currentPlayer = GameCore.playerList.get(GameCore.getCurrentPlayer());
        
            // Move the player
            int diceResult = dice.getDiceResult();
            int oldPosition = currentPlayer.getPosition(); // Store the old position
            currentPlayer.move(diceResult);
        
            // Update the player's position on the board
            int newPosition = currentPlayer.getPosition();
            BoardProperty landedProperty = GameCore.bProperties.get(newPosition);
            landedProperty.playerLanded(currentPlayer, diceResult);
        
            // Update the player info label
            playerInfoLabel.setText(getCurrentPlayerInfo());
        
            // Update the player tokens on the board
            updatePlayerToken(oldPosition, newPosition, currentPlayer);
        
            // Cycle to the next player
            GameCore.cycleNextPlayer();
        });
    
        // Add the dice result label and button to the dice panel
        dicePanel.add(diceResultLabel);
        dicePanel.add(rollDiceButton);
    
        // Add the dice panel to the board
        gbc.gridy = 2;
        boardPan.add(dicePanel, gbc);
    }

    // Add this method to update player tokens on the board
    private void updatePlayerToken(int oldPosition, int newPosition, Player player) {
        // Remove the token from the old position
        int oldX = getTileXFromPosition(oldPosition);
        int oldY = getTileYFromPosition(oldPosition);
        JPanel oldTile = (JPanel) leftPanel.getComponentAt(oldX, oldY);
        if (oldTile != null) {
            oldTile.removeAll();
            overlayPlayerTokens(oldTile, oldX, oldY);
        }

        // Add the token to the new position
        int newX = getTileXFromPosition(newPosition);
        int newY = getTileYFromPosition(newPosition);
        JPanel newTile = (JPanel) leftPanel.getComponentAt(newX, newY);
        if (newTile != null) {
            overlayPlayerTokens(newTile, newX, newY);
        }

        // Revalidate and repaint the board
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // Add helper methods to calculate tile coordinates from position
    private int getTileXFromPosition(int position) {
        // Logic to calculate X coordinate based on position
        return (position < 10) ? position : (position < 20) ? 10 : (position < 30) ? 30 - position : 0;
    }

    private int getTileYFromPosition(int position) {
        // Logic to calculate Y coordinate based on position
        return (position < 10) ? 0 : (position < 20) ? position - 10 : (position < 30) ? 10 : 40 - position;
    }

    // Helper method to get the current player's information as a formatted string
    private String getCurrentPlayerInfo() {
        Player currentPlayer = GameCore.playerList.get(GameCore.getCurrentPlayer());
        return String.format(
            "<html><h2>Player: %s<br>Money: $%d<br>Position: %d</h2></html>",
            currentPlayer.getName(),
            currentPlayer.getMoney(),
            currentPlayer.getPosition()
        );
    }

   // Adds a section to the info panel with interactive property panels
    private void addInfoPanelSection(JPanel boardPan, int gridy, Dimension size) {
        JPanel panel = createPanel(new JPanel(), size);
        panel.setLayout(new GridLayout(0, 4, 5, 5)); // Grid layout for property panels
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create a small panel for each BoardProperty
        for (BoardProperty property : GameCore.bProperties) {
            PropertyPanel propertyPanel = new PropertyPanel(property);
            panel.add(propertyPanel.getPanel());
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.fill = GridBagConstraints.BOTH;
        boardPan.add(panel, gbc);
    }

    // Calculates the board position based on x and y coordinates
    private int getPositionFromTile(int x, int y) {
        return (y == 0) ? x : (y == 10) ? 30 - x : (x == 0) ? 40 - y : 10 + y;
    }

    // Retrieves the image for a tile based on its type
    private ImageIcon getTileImage(BoardProperty.propertyType type, int width, int height) {
        String imagePath = switch (type) {
            case GO -> "images/go.png";
            case JAIL_CELL -> "images/jail.png";
            case FREE_PARKING -> "images/free_parking.png";
            case JAIL -> "images/go_to_jail.png";
            case CHANCE -> "images/chance.png";
            case COMMUNITY -> "images/community_chest.png";
            case RAIL -> "images/reading_railroad.png";
            case ELECTRIC -> "images/electric.jpg";
            case WATER -> "images/water_works.jpg";
            case INCOME_TAX -> "images/income_tax.png";
            case SUPER_TAX -> "images/luxury.jpg";
            default -> null;
        };
        if (imagePath == null) return null;
        Image img = new ImageIcon(imagePath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // Retrieves the image for a tile based on its type
    private ImageIcon getTokenImage(Player.Token t) {
        String imagePath = switch (t) {
            case MONEY -> "images/money.webp";
            case CAR -> "images/car.jpeg";
            case HAT -> "images/hat.jpeg";
            case DOG -> "images/dog.jpeg";
            case THIMBLE -> "images/thimble.jpeg";
            case CAT -> "images/cat.webp";
            case PENGUIN -> "images/penguin.png";
            default -> null;
        };
        if (imagePath == null) return null;
        Image img = new ImageIcon(imagePath).getImage().getScaledInstance(25,25, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // Retrieves the color for a property based on its type
    private Color getPropertyColor(BoardProperty.color property) {
        return switch (property) {
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

    // Custom JPanel to draw the background image
    class BackgroundPanel extends JPanel {
        private final Image image;

        public BackgroundPanel(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}