package it.polimi.ingsw.exceptions;

/**
 * Class GameException is thrown when a generic error is given by the execution of an error.
 */
public class GameException extends Exception {

    protected String errorMessage;

    /**
     * Constructor GameException creates the GameException instance, and sets its custom message.
     * @param errorMessage the message to be set to the GameException.
     */
    public GameException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Method getMessage returns the message of the exception.
     */
    @Override
    public String getMessage() {
        return errorMessage;
    }
}