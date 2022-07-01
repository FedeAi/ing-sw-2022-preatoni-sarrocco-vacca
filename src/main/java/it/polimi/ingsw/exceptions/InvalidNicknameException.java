package it.polimi.ingsw.exceptions;


/**
 * @see Exception
 */
public class InvalidNicknameException extends Exception {
    @Override
    public String getMessage(){
        return ("Error: the chosen nickname contains invalid characters.");
    }
}

