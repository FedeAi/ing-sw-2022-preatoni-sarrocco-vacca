package it.polimi.ingsw.server.answers.model;

import it.polimi.ingsw.model.cards.AssistantCard;

/**
 * PlayedCardMessage class is type of ModelMessage used for sending updates of the current cards that have been played.
 *
 * @see ModelMessage
 */
public class PlayedCardMessage implements ModelMessage {
    private final AssistantCard message;
    private final String player;

    /**
     * Constructor PlayedCardMessage creates a new MoveMessage instance.
     *
     * @param player  the name of the player that has played the card.
     * @param message the card played.
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

    /**
     * Method getPlayer returns the nickname of the player that has played the card.
     *
     * @return the nickname of the player.
     */
    public String getPlayer() {
        return player;
    }
}