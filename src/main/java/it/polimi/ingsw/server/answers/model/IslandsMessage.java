package it.polimi.ingsw.server.answers.model;

import it.polimi.ingsw.model.islands.IslandContainer;

/**
 * IslandsMessage class is type of ModelMessage used for sending updates of the game's islands.
 *
 * @see ModelMessage
 */
public class IslandsMessage implements ModelMessage {
    private final IslandContainer message;

    /**
     * Constructor IslandsMessage creates a new IslandsMessage instance.
     *
     * @param islandContainer the game's islands.
     */
    public IslandsMessage(IslandContainer islandContainer) {
        this.message = islandContainer;
    }

    /**
     * Method getMessage returns the message of this Answer object.
     *
     * @return the message (type Object) of this Answer object.
     * @see ModelMessage#getMessage()
     */
    @Override
    public IslandContainer getMessage() {
        return message;
    }
}