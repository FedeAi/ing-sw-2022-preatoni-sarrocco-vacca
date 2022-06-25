package it.polimi.ingsw.server.answers.model;

import it.polimi.ingsw.constants.Magician;

import java.util.Map;

/**
 * MagicianMessage class is type of ModelMessage used for sending updates of the game's magicians.
 *
 * @author Davide Preatoni, Federico Sarrocco
 * @see ModelMessage
 */
public class MagicianMessage implements ModelMessage {

    private final Map<Magician, String> message;

    /**
     * Constructor MoveMessage creates a new MoveMessage instance.
     *
     * @param magicians the Magician ownership map
     */
    public MagicianMessage(Map<Magician, String> magicians) {
        this.message = magicians;
    }

    /**
     * Method getMessage returns the message of this Answer object.
     *
     * @return the message (type Object) of this Answer object.
     * @see ModelMessage#getMessage()
     */
    @Override
    public Map<Magician, String> getMessage() {
        return message;
    }
}