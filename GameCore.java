import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public final class GameCore {
    // Player and property
    public static ArrayList<BoardProperty> bProperties = new ArrayList<>(); 
    public static ArrayList<Player> playerList = new ArrayList<>(); 
    private static int PlayerCount;
    private static int currentPlayer = 0;

    // Constructor for Game core
    public GameCore() {
        loadGameProperties("data/gameProperties.dat");
        requestPlayerCount();
        playerInit();
    }

    public static int getCurrentPlayer() {
        return currentPlayer;
    }

    public static void cycleNextPlayer() {
        currentPlayer++;
        if(currentPlayer >= PlayerCount){
            currentPlayer = 0;
        }
    }

    // Function that asks the player what Token they would like their player to be
    private void playerInit(){
        String[] options = {"Sack of Money", "Race Car", "Top Hat", "Thimble", "Scottish Terrier", "Cat", "Penguin", "Rubber Duck"};
        for(int i = 0; i < PlayerCount; i++){
            String message = "Choose your token Player " + (i+1) + ":"; 
            int choice = JOptionPane.showOptionDialog(
                null,
                message,
                "Monopoly",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            playerList.add(new Player("P" + choice + " " + options[choice], switch(options[choice]){
                case "Sack of Money" -> Player.Token.MONEY;
                case "Race Car" -> Player.Token.CAR;
                case "Top Hat" -> Player.Token.HAT;
                case "Thimble" -> Player.Token.THIMBLE;
                case "Scottish Terrier" -> Player.Token.DOG;
                case "Cat" -> Player.Token.CAT;
                case "Penguin" -> Player.Token.PENGUIN;
                case "Rubber Duck" -> Player.Token.DUCK;
                default -> Player.Token.NONE;
            }));

            options = removeElement(options, choice);
        }
    }

    // Temporary function to remove an index inside a immutable array
    private static String[] removeElement(String[] arr, int index) {
        if (index < 0 || index >= arr.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        String[] newArr = new String[arr.length - 1];
        System.arraycopy(arr, 0, newArr, 0, index);
        System.arraycopy(arr, index + 1, newArr, index, arr.length - index - 1);
        return newArr;
    }

    // Function that asks the player how many people are playing in this session
    private void requestPlayerCount(){
        // This function nearly made me go insane!
        String count = JOptionPane.showInputDialog(null,"How many players? [2-8]");
        while (!count.matches("\\d+") || Integer.parseInt(count) < 2 || Integer.parseInt(count) > 8) {
            count = JOptionPane.showInputDialog("Please enter a valid number:");
        }
        PlayerCount = Integer.parseInt(count);
        System.out.println(PlayerCount);
    }

    // Function that loads the board properties from a data file
    private static void loadGameProperties(String fileDir){
        try{
            File propertiesFile = new File(fileDir);
            try (Scanner readObj = new Scanner(propertiesFile)) {
                while(readObj.hasNextLine()){

                    String name;
                    BoardProperty.propertyType type;
                    BoardProperty.color color;
                    int cost;
                    ArrayList<Integer> rent = new ArrayList<>();

                    name = readObj.nextLine();
                    cost = readObj.nextInt();
                    readObj.nextLine();
                    type = switch (readObj.nextLine()) {
                        case "REGULAR" -> BoardProperty.propertyType.REGULAR;
                        case "COMMUNITY" -> BoardProperty.propertyType.COMMUNITY;
                        case "CHANCE" -> BoardProperty.propertyType.CHANCE;
                        case "RAIL" -> BoardProperty.propertyType.RAIL;
                        case "ELECTRIC" -> BoardProperty.propertyType.ELECTRIC;
                        case "WATER" -> BoardProperty.propertyType.WATER;
                        case "INCOME_TAX" -> BoardProperty.propertyType.INCOME_TAX;
                        case "SUPER_TAX" -> BoardProperty.propertyType.SUPER_TAX;
                        case "JAIL" -> BoardProperty.propertyType.JAIL;
                        case "JAIL_CELL" -> BoardProperty.propertyType.JAIL_CELL;
                        case "FREE_PARKING" -> BoardProperty.propertyType.FREE_PARKING;
                        case "GO" -> BoardProperty.propertyType.GO;
                        default ->  BoardProperty.propertyType.FREE_PARKING;
                    };
                    while(readObj.hasNextInt()){
                        rent.add(readObj.nextInt());
                    }
                    readObj.nextLine();
                    color = switch(readObj.nextLine()){
                        case "BLUE" -> BoardProperty.color.BLUE;
                        case "GREEN" -> BoardProperty.color.GREEN;
                        case "YELLOW" -> BoardProperty.color.YELLOW;
                        case "RED" -> BoardProperty.color.RED;
                        case "ORANGE" -> BoardProperty.color.ORANGE;
                        case "PINK" -> BoardProperty.color.PINK;
                        case "BABY_BLUE" -> BoardProperty.color.BABY_BLUE;
                        case "BROWN" -> BoardProperty.color.BROWN;
                        case "RAIL" -> BoardProperty.color.RAIL;
                        case "ELECTRIC" -> BoardProperty.color.ELECTRIC;
                        case "WATER" -> BoardProperty.color.WATER;
                        case "NONE" -> BoardProperty.color.NONE;
                        default ->  BoardProperty.color.NONE;
                    };
                    readObj.nextLine();
                    int r[] = new int[rent.size()];
                    for(int i = 0; i < rent.size(); i++){
                        r[i] = rent.get(i);
                    }
                    bProperties.add(new BoardProperty(name, cost, type, r, color));
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("An Error has occured");
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        GameCore coreGame = new GameCore();
        MonopolyBoard board = new MonopolyBoard();
    }

    @Override
    public String toString(){
        return "Player Count:" + PlayerCount + "\nPlayers:" + playerList.toString() + "\nProperties:" + bProperties.toString();
    }

    
}
