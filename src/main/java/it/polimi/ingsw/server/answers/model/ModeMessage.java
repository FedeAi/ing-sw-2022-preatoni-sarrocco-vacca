package it.polimi.ingsw.server.answers.model;

/**
 * ModeMessage class is type of ModelMessage used for sending the game's current mode.
 *
 * @see ModelMessage
 */
public class ModeMessage implements ModelMessage {
    private final Boolean message;

    /**
     * Constructor MoveMessage creates a new MoveMessage instance.
     *
     * @param isExpert the boolean value of the current game mode
     */
    public ModeMessage(Boolean isExpert) {
        this.message = isExpert;
    }

    /**
     * Method getMessage returns the message of this Answer object.
     * It returns the match mode
     *
     * @return the message (type Object) of this Answer object.
     * @see ModelMessage#getMessage()
     */
    @Override
    public Boolean getMessage() {
        return message;
    }
}