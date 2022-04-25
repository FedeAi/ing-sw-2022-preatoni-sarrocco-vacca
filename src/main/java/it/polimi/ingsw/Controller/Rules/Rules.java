package it.polimi.ingsw.Controller.Rules;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.DynamicRules;
import it.polimi.ingsw.Model.Enumerations.Color;


public class Rules {

    public static final int maxPlayers = 4;
    public static final int maxIslands = 12;
    public static final int initialBagSize = 2 * Color.values().length;
    public static final int bagSize = 130;
    public static final int numCharacterCards = 3;
    public static final int numAssistantCards = 10;


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
