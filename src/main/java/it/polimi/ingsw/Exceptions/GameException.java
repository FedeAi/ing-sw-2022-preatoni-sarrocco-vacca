package it.polimi.ingsw.Exceptions;

public class GameException extends Exception {
    protected String errorMessage;
    public GameException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
