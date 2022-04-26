package it.polimi.ingsw.Controller.Rules;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.DynamicRules;
import it.polimi.ingsw.Model.Enumerations.Color;


public class Rules {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    public static final int maxIslands = 12;
    public static final int initialBagSize = 2 * Color.values().length;
    public static final int bagSize = 130;
    public static final int numCharacterCards = 3;
    public static final int numAssistantCards = 10;
    public static final int numCoins = 20;
    public static final int initialPlayerBalance = 1;


    private DynamicRules dynamicRules;

    public Rules() {
        dynamicRules = new BaseRules();
    }

    public static int getEntrySize(int numPlayers) {
        return 9;
    }

    public static int getStudentsPerTurn(int numPlayers) {

        return switch (numPlayers) {
            case 2 -> 3;
            case 3 -> 4;
            default -> 3;
        };
    }

    public static int getTowersPerPlayer(int numPlayers) {
        return switch (numPlayers) {
            case 2 -> 8;
            case 3 -> 6;
            default -> 8;
        };

    }

    /**
     * This method returns true if the position on the school hall has a coin
     * @param position hall's index
     * @return returns true if the position on the school hall has a coin, otherwise false
     */
    public static boolean checkCoin(int position) {
        return switch (position) {
            case 2,5,8 -> true;
            default -> false;
        };
    }

    public int getNumAssistantCards(){
        return numAssistantCards;
    }

    public DynamicRules getDynamicRules() {
        return dynamicRules;
    }

    public void setDynamicRules(DynamicRules dynamicRules) {
        this.dynamicRules = dynamicRules;
    }
}
