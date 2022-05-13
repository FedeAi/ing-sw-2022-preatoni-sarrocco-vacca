package it.polimi.ingsw.Exceptions;

public class GameException extends Exception {
    private String errorMessage;
    public GameException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
