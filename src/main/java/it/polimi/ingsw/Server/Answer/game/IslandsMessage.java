package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Server.Answer.Answer;

/**
 * IslandMessage class is an Answer used for ........
 *
 * @author GC30
 * @see Answer
 */
public class IslandsMessage implements Answer {
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
     * @see Answer#getMessage()
     */
    @Override
    public IslandContainer getMessage() {
        return message;
    }


}