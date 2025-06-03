import java.util.ArrayList;
import java.util.Arrays;

public class BoardProperty {
    // Values required for each property
    private String name;
    private Player owner;
    private int[] rentRates;
    private int propertyCost;
    private int houseCount = 0;
    private propertyType type;
    private final color colorType;
    private boolean isMortgage = false;

    public enum color{
        BLUE, GREEN, YELLOW, RED, ORANGE, PINK, BABY_BLUE, BROWN, RAIL, ELECTRIC, WATER, NONE
    }

    public enum propertyType{
        REGULAR, COMMUNITY, CHANCE, RAIL, ELECTRIC, WATER, INCOME_TAX, SUPER_TAX, JAIL, JAIL_CELL, FREE_PARKING, GO
    }

    public void mortgage() {
        if (!isMortgage && owner != null) {
            isMortgage = true;
            owner.addMoney(propertyCost / 2); // Mortgage value is half the property cost
        }
    }
    
    public void repayMortgage() {
        if (isMortgage && owner != null && owner.canAfford(propertyCost / 2)) {
            isMortgage = false;
            owner.deductMoney(propertyCost / 2);
        }
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public int getHouseCount() {
        return houseCount;
    }

    // Contructor for all properties regardless if it charges property or not.
    public BoardProperty(String n, int c, propertyType t, int[] rates, color colorT){
        name = n;
        propertyCost = c;
        type = t;
        rentRates = rates;
        colorType = switch (t) {
            case RAIL -> BoardProperty.color.RAIL;
            case ELECTRIC -> BoardProperty.color.ELECTRIC;
            case WATER -> BoardProperty.color.WATER;
            default -> colorT;
        };
    }

    // Method allowing the player to purchace the property (Lacks removing money functionality)
    public void changeOwner(Player newOwner){
        owner = newOwner;
        owner.addProperty(this);
    }

    // Method that allows someone to purchase houses
    public void addHouse(){
        if(owner == null){
            return;
        }
        if(!checkIfSetComplete(colorType,owner)){
            return;
        }
        if(houseCount >= 5){
            return;
        }
        if(owner.canAfford(switch (colorType) {
            case BROWN, BABY_BLUE -> 50;
            case PINK, ORANGE -> 100;
            case RED, YELLOW -> 150;
            case GREEN, BLUE -> 200;
            default -> 0;})){
            owner.deductMoney(switch (colorType) {
                case BROWN, BABY_BLUE -> 50;
                case PINK, ORANGE -> 100;
                case RED, YELLOW -> 150;
                case GREEN, BLUE -> 200;
                default -> 0;});
            houseCount++;
        }
    }

    // Method that allows someone to sell houses
    public void removeHouse(){
        if(owner == null){
            return;
        }
        if(!checkIfSetComplete(colorType,owner)){
            return;
        }
        if(houseCount < 1){
            return;
        }
        owner.addMoney(switch (colorType) {
            case BROWN, BABY_BLUE -> 25;
            case PINK, ORANGE -> 50;
            case RED, YELLOW -> 75;
            case GREEN, BLUE -> 100;
            default -> 0;});
        houseCount--;
    }

    // Method that charges rent or tells player to buy the property
    public void playerLanded(Player p, int diceRoll){
        if(owner == null){
            switch (type) {
                case REGULAR, RAIL, ELECTRIC, WATER -> purchaceProperty(p);
                case INCOME_TAX, SUPER_TAX -> specialProperty(p, type);
                default -> {}
            }
            return;
        }
        //chargeRent(Player p, Player owner, int[] r, int h, propertyType t, int colorCount)
        switch (type) {
            case REGULAR, RAIL, ELECTRIC, WATER -> chargeRent(p, owner, rentRates, houseCount, type, diceRoll);
            default -> {}
        }
    }

    // Purchase or auction an unowned property when someone passes by.
    private void purchaceProperty(Player p){
        if(p.canAfford(propertyCost)){
            p.deductMoney(propertyCost);
            changeOwner(p);
        }else{
            // TODO: Auction functionality
        }
    }

    // Has custom property rent cost every time somone steps on the property
    private void specialProperty(Player p, propertyType t){
        switch (t) {
            case INCOME_TAX -> p.deductMoney(200);
            case SUPER_TAX -> p.deductMoney(75);
            default -> {}
        }
    }

    // charges a specific amount of rent depending on the rent shown in the card
    private void chargeRent(Player p, Player owner, int[] r, int h, propertyType t, int roll){
        if (isMortgage) {
            return;
        }
        switch (t) {
            case REGULAR:
                p.deductMoney(r[h]*((checkIfSetComplete(colorType,owner) && houseCount == 0)?2:1));
                owner.addMoney(r[h]*((checkIfSetComplete(colorType,owner) && houseCount == 0)?2:1));
                break;
            case ELECTRIC:
                p.deductMoney((getAmountofColor(BoardProperty.color.ELECTRIC, owner)>=2)?roll*4:roll*10);
                owner.addMoney((getAmountofColor(BoardProperty.color.ELECTRIC, owner)>=2)?roll*4:roll*10);
            case WATER:
                p.deductMoney((getAmountofColor(BoardProperty.color.WATER, owner)>=2)?roll*4:roll*10);
                owner.addMoney((getAmountofColor(BoardProperty.color.WATER, owner)>=2)?roll*4:roll*10);
                break;
            case RAIL:
                p.deductMoney(r[getAmountofColor(BoardProperty.color.RAIL, owner)]);
                owner.addMoney(r[getAmountofColor(BoardProperty.color.RAIL, owner)]);
            default:
                break;
        }
    }

    // checks if set off a specific color is complete (doesnt work for utilities)
    private boolean checkIfSetComplete(color colorType,Player owner){
        int count = getAmountofColor(colorType, owner);
        return switch (colorType) {
            case RED, YELLOW, GREEN, ORANGE, PINK, BABY_BLUE -> (count >= 3);
            case BLUE, BROWN -> (count >= 2);
            default -> false;
        };
    }

    // counts how many of a specific property color there is.
    private int getAmountofColor(color colorType,Player owner){
        int count = 0;
        ArrayList<BoardProperty> propertiesList = owner.getProperties();
        for(int i = 0; i < owner.getProperties().size(); i++){
            count += (propertiesList.get(i).colorType == colorType)?1:0;
        }
        return count;
    }
    public static void main(String[] args) {

    }

    // Getter methods
    public propertyType getProperType(){
        return type;
    }
    public color getProperColor(){
        return colorType;
    }

    public String getProperName(){
        return name;
    }

    public int getProperCost(){
        return propertyCost;
    }

    @Override
    public String toString() {
        return name + " [Owner: " + ((owner==null)?"Bank":owner.getName()) + ", Rent: " + Arrays.toString(rentRates) + ", House count: " + houseCount + ", Cost: " + propertyCost + "]";
    }
    
}
