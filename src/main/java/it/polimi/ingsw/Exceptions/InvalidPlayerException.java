package it.polimi.ingsw.Exceptions;

/**
 * Class GameException is thrown when an invalid player is given to an action.
 */
public class InvalidPlayerException extends Exception {

    /**
     * Method getMessage returns the custom InvalidPlayerException message.
     */
    @Override
    public String getMessage() {
        return "Specified player not present.";
    }
}
