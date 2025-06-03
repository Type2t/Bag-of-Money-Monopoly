import java.util.ArrayList;

public class Player {
    private String name;
    private int position;
    private int money;
    private ArrayList<BoardProperty> properties;
    private Token token;
    private boolean isInJail;

    public enum Token {
        MONEY, CAR, HAT, DOG, THIMBLE, CAT, PENGUIN, DUCK, NONE
    }

    // Constructor
    public Player(String name, Token token) {
        this.name = name;
        this.position = 0;  // Starting position on the board
        this.money = 1500;  // Default Monopoly money
        this.properties = new ArrayList<>();
        this.token = token;
        this.isInJail = false;  // Default state is not in jail
    }
    

    // Getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ArrayList<BoardProperty> getProperties() {
        return properties;
    }

    public void addProperty(BoardProperty property) {
        this.properties.add(property);
    }

    public void removeProperty(BoardProperty property) {
        this.properties.remove(property);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    // Move player based on dice roll
    public void move(int diceResult) {
        if (isInJail) {
            return;
        }
        this.position += diceResult;
        if (this.position >= 40) {  // Wrap around the board (Monopoly has 40 spaces)
            this.position -= 40;
            this.money += 200;  // Pass GO and collect $200
        }
    }

    // Deduct money from player (for example, paying rent or taxes)
    public void deductMoney(int amount) {
        this.money -= amount;
    }

    // Add money to player (for example, receiving rent or passing GO)
    public void addMoney(int amount) {
        this.money += amount;
    }

    public boolean isIsInJail() {
        return isInJail;
    }

    public void setIsInJail(boolean isInJail) {
        this.isInJail = isInJail;
        if(this.isInJail) {
            this.position = 10;  // Assuming position 10 is Jail
        }
    }

    // Check if the player can afford a certain amount
    public boolean canAfford(int amount) {
        return this.money >= amount;
    }

    // Display player's information
    @Override
    public String toString() {
        return name + " [Position: " + position + ", Money: $" + money + ", Properties: " + properties + "]";
    }

    // Override equals() and hashCode() if needed
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equals(player.name);  // Or use token if you prefer
    }

    @Override
    public int hashCode() {
        return name.hashCode();  // Or use token
    }
}