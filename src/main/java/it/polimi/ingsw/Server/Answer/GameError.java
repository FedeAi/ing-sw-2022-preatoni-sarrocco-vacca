package it.polimi.ingsw.Server.Answer;

import it.polimi.ingsw.Constants.ErrorType;

public class GameError implements Answer {
    private final ErrorType error;
    private final String message;

    public GameError(String message) {
        this.message = message;
        error = null;
    }

    public GameError(ErrorType error) {
        this.error = error;
        this.message = null;
    }

    public GameError(ErrorType error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorType getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
