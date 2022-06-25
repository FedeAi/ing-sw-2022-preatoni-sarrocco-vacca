package it.polimi.ingsw.exceptions;

/**
 * Class RoundOwnerException is thrown when the player associated with the action is not the current round owner.
 */
public class RoundOwnerException extends Exception {

    private String currentOwner;

    /**
     * Constructor RoundOwnerException creates the RoundOwnerException, and sets its parameter.
     *
     * @param currentOwner the current round owner.
     */
    public RoundOwnerException(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    /**
     * Method getMessage returns the properly formatted RoundOwnerException message.
     */
    @Override
    public String getMessage() {
        return "It's not your turn! The current player is " + currentOwner + ".";
    }
}
