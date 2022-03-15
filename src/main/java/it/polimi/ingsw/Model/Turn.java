package it.polimi.ingsw.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Turn {
    private Player currentPlayer;
    private ArrayList<AssistantCard> cards;
    private ArrayList<Cloud> clouds;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //Returns a list of cards that have already been played
    // Useful for the next player at the beginning of his turn

    public ArrayList<AssistantCard> getPlayedCards() {
        return cards;
    }
    //Returns the clouds available for the next player
    public ArrayList<Cloud> getAvailableClouds() {
        return clouds;
    }
}

