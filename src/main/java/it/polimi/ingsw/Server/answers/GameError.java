package it.polimi.ingsw.Server.answers;

import it.polimi.ingsw.Constants.ErrorType;

/**
 * Class GameError is a type of Answer used for sending different types of errors.
 */
public class GameError implements Answer {

    private final ErrorType error;
    private final String message;

    /**
     * Constructor GameError creates a new GameError instance.
     *
     * @param message the message to be printed on the client's terminal.
     */
    public GameError(String message) {
        this.message = message;
        error = null;
    }

    /**
     * Constructor GameError creates a new GameError instance.
     *
     * @param error the type of error to display.
     */
    public GameError(ErrorType error) {
        this.error = error;
        this.message = null;
    }

    /**
     * Constructor GameError creates a new GameError instance.
     *
     * @param message the message to be printed on the client's terminal.
     * @param error the type of error to display.
     */
    public GameError(ErrorType error, String message) {
        this.error = error;
        this.message = message;
    }

    /**
     * Method getError returns the GameError's error type.
     */
    public ErrorType getError() {
        return error;
    }

    /**
     * Method getMessage returns the GameError's message.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
