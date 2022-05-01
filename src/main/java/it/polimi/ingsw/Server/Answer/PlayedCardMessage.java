package it.polimi.ingsw.Server.Answer;

import it.polimi.ingsw.Constants.Pair;
import it.polimi.ingsw.Model.Cards.AssistantCard;

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
    private final Pair<String, AssistantCard> message;

    /**
     * Constructor MoveMessage creates a new MoveMessage instance.
     *
     * @param playedCard .................
     */

    public PlayedCardMessage(Pair<String, AssistantCard> message) {
        this.message = message;
    }

    /**
     * Method getMessage returns the message of this WorkerPlacement object.
     *
     * @return the message (type Object) of this WorkerPlacement object.
     * @see Answer#getMessage()
     */
    @Override
    public Pair<String, AssistantCard> getMessage() {
        return message;
    }
}
