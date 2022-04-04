package it.polimi.ingsw.Controller;

public class Rules {
    static final int maxPlayers = 4;
    static final int maxIslands = 12;

    public static int getEntrySize(int numPlayers){
        return 9;
    }
    public static int getStudentsPerTurn(int numPlayers){
        return 4;
    }
}
