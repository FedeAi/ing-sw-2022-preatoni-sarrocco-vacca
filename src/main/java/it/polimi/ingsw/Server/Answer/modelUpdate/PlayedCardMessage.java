package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.Cards.AssistantCard;

/**
 * PlayedCardMessage class is a ModelMessage used for sending infos about the cards that have been played to the client.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see ModelMessage
 */
public class PlayedCardMessage implements ModelMessage {
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
     * @see ModelMessage#getMessage()
     */
    @Override
    public AssistantCard getMessage() {
        return message;
    }

    public String getPlayer() {
        return player;
    }
}
