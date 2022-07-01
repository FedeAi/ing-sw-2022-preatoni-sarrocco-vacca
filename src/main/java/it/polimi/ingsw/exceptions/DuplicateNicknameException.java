package it.polimi.ingsw.exceptions;

/**
 * Class DuplicateNicknameException is thrown when a player
 * tries to register to a new or existing game lobby with an already used nickname.
 *
 * @see Exception
 */
public class DuplicateNicknameException extends Exception {

    /**
     * Method getMessage returns the custom DuplicateNicknameException message.
     */
    @Override
    public String getMessage() {
        return ("Error: the chosen nickname has already been taken. Nicknames must be unique.");
    }
}