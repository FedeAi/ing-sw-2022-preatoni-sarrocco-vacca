package it.polimi.ingsw.Controller.Rules;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.DynamicRules;

/**
 * Rules class represents the game's rules, that get dynamically updated by the character cards.
 */
public class Rules {
    private DynamicRules dynamicRules;

    /**
     * Constructor Rules creates a Rules instance with an initial BaseRules instance.
     */
    public Rules() {
        dynamicRules = new BaseRules();
    }

    /**
     * Method getEntrySize returns the size of a player's entry.
     *
     * @param numPlayers the number of players in the game.
     */
    public static int getEntrySize(int numPlayers) {
        return 9;
    }

    /**
     * Method getStudentsPerTurn returns the number of students allowed per turn.
     *
     * @param numPlayers the number of players in the game.
     * @return 3 if the game is a 2 player one, 4 if it is a 3 player one.
     */
    public static int getStudentsPerTurn(int numPlayers) {
        return switch (numPlayers) {
            case 2 -> 3;
            case 3 -> 4;
            default -> 3;
        };
    }

    /**
     * Method getTowersPerPlayer returns the initial number of towers to be given to each of the players.
     *
     * @param numPlayers the number of players in the gmae.
     * @return 8 if the game is a 2 player one, 6 if it is a 3 player one.
     */
    public static int getTowersPerPlayer(int numPlayers) {
        return switch (numPlayers) {
            case 2 -> 8;
            case 3 -> 6;
            default -> 8;
        };

    }

    /**
     * Method checkCoin checks if the position on the school hall has a coin on it (expert mode related).
     *
     * @param position the hall index to be checked
     * @return true if the position on the school hall has a coin, false otherwise.
     */
    public static boolean checkCoin(int position) {
        return switch (position) {
            case 2, 5, 8 -> true;
            default -> false;
        };
    }

    /**
     * Method getNumAssistantCards returns the initial number of the AssistantCard to be given to the player.
     */
    public int getNumAssistantCards() {
        return Constants.NUM_ASSISTANT_CARDS;
    }

    /**
     * Method getDynamicRules returns the current instance of DynamicRules.
     *
     * @see DynamicRules
     */
    public DynamicRules getDynamicRules() {
        return dynamicRules;
    }

    /**
     * Method setDynamicRules sets the current instance of DynamicRules to a new one (character cards related).
     *
     * @see DynamicRules
     */
    public void setDynamicRules(DynamicRules dynamicRules) {
        this.dynamicRules = dynamicRules;
    }
}