package it.polimi.ingsw.Constants.Exceptions;

/**
 * @author Alessandro Vacca
 * @see Exception
 */
public class DuplicateNicknameException extends Exception {
    @Override
    public String getMessage() {
        return ("Error: the chosen nickname has already been taken. Nicknames must be unique.");
    }
}

