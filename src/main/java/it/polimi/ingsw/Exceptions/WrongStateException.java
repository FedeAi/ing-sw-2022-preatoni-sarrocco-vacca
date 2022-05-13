package it.polimi.ingsw.Exceptions;


public class WrongStateException extends GameException {

    public WrongStateException(String state) {
        super(state);
    }

    @Override
    public String getMessage() {
        return "This action is reserved for the " + errorMessage;
    }
}
