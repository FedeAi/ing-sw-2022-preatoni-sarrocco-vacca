package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Constants.Pair;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Server.Answer.Answer;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * PlayedCardMessage class is an Answer used for sending infos about the cards that have been played to the client.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see Answer
 */
public class PlayedCardMessage implements Answer {
    private final AssistantCard message;
    private final String player;

    /**
     * Constructor MoveMessage creates a new MoveMessage instance.
     *
     * @param playedCard .................
     */

    public PlayedCardMessage(String player, AssistantCard message) {
        this.player = player;
        this.message = message;
    }

    /**
     * Method getMessage returns the message of this Answer object.
     *
     * @return the message (type Object) of this Answer object.
     * @see Answer#getMessage()
     */
    @Override
    public AssistantCard getMessage() {
        return message;
    }

    public String getPlayer() {
        return player;
    }
}
