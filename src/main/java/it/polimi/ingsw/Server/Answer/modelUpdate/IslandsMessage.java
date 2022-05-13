package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.Islands.IslandContainer;

/**
 * IslandMessage class is a ModelMessage used for ........
 *
 * @author GC30
 * @see ModelMessage
 */
public class IslandsMessage implements ModelMessage {
    private final IslandContainer message;

    /**
     * Constructor MoveMessage creates a new MoveMessage instance.
     *
     * @param islandContainer .................
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