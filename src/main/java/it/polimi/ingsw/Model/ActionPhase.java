package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class ActionPhase {
    private ArrayList<Player> orderedPlayers;
    private ArrayList<AssistantCard> cards;
    private ArrayList<Cloud> clouds;
    private int currentPlayer;

    // extractionNumber is the number of students to be put on the cloud. 3 for 2/4P, 4 for 3P.
    public ActionPhase(int extractionNumber, ArrayList<Player> players, Bag bag) {
        clouds = new ArrayList<>();
        for(int i = 0; i < players.size(); i++) {
            clouds.add(new Cloud(bag.extract(extractionNumber)));
        }
    }

    public Player getCurrentPlayer() {
        return orderedPlayers.get(currentPlayer);
    }

    // Returns a list of cards that have already been played
    // Useful for the next player at the beginning of his turn
    public ArrayList<AssistantCard> getPlayedCards() {
        return cards;
    }
    // Returns the clouds available for the next player
    public ArrayList<Cloud> getAvailableClouds() {
        return clouds;
    }
}

