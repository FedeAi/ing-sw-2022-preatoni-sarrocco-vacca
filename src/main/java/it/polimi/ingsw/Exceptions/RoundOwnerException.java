package it.polimi.ingsw.Exceptions;

public class RoundOwnerException extends Exception {
    private String currentOwner;

    public RoundOwnerException(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    @Override
    public String getMessage() {
        return "It's not your turn! The current player is " + currentOwner;
    }
}
