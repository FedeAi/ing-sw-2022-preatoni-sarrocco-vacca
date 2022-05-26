package it.polimi.ingsw.Exceptions;

public class InvalidPlayerException extends Exception {
    @Override
    public String getMessage() {
        return "Specified player not present.";
    }
}
