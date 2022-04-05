package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.Color;

public class Rules {
    static final int maxPlayers = 4;
    static final int maxIslands = 12;
    static final int initialBagSize = 2 * Color.values().length;
    static final int bagSize = 130;

    public static int getEntrySize(int numPlayers){
        return 9;
    }
    public static int getStudentsPerTurn(int numPlayers){
        return 4;
    }
}
