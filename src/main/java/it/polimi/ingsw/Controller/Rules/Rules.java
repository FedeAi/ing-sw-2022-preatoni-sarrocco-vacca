package it.polimi.ingsw.Controller.Rules;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.DynamicRules;
import it.polimi.ingsw.Constants.Color;


public class Rules {
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
     *
     * @param position hall's index
     * @return returns true if the position on the school hall has a coin, otherwise false
     */
    public static boolean checkCoin(int position) {
        return switch (position) {
            case 2, 5, 8 -> true;
            default -> false;
        };
    }

    public int getNumAssistantCards() {
        return Constants.NUM_ASSISTANT_CARDS;
    }

    public DynamicRules getDynamicRules() {
        return dynamicRules;
    }

    public void setDynamicRules(DynamicRules dynamicRules) {
        this.dynamicRules = dynamicRules;
    }
}
