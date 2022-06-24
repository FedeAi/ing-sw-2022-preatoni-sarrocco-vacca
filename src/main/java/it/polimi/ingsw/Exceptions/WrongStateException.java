package it.polimi.ingsw.Exceptions;

/**
 * Class WrongStateException is thrown when an action is executed with the wrong game state set.
 */
public class WrongStateException extends GameException {

    /**
     * Constructor WrongStateException creates the RoundOwnerException, and sets its parameter.
     *
     * @param state the required game state.
     */
    public WrongStateException(String state) {
        super(state);
    }

    /**
     * Method getMessage returns the properly formatted WrongStateException message.
     */
    @Override
    public String getMessage() {
        return "This action is reserved for the " + errorMessage;
    }
}