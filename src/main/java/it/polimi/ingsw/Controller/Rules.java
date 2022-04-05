package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.Color;

public class Rules {
    public static final int maxPlayers = 4;
    public static final int maxIslands = 12;
    public static final int initialBagSize = 2 * Color.values().length;
    public static final int bagSize = 130;

    public static int getEntrySize(int numPlayers){
        return 9;
    }
    public static int getStudentsPerTurn(int numPlayers){
        return 4;
    }
    public static int getTowersPerPlayer(int numPlayers){
        int towers = 8;
        switch(numPlayers){

            case 2:
                towers = 8;
                break;
            case 3:
                towers = 6;
                break;
            default:
                towers = 8;
        }
        return towers;

    }
}
