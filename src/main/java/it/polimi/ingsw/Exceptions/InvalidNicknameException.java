package it.polimi.ingsw.Exceptions;


/**
 * @author Federico Sarrocco
 * @see Exception
 */
public class InvalidNicknameException extends Exception {
    @Override
    public String getMessage(){
        return ("Error: the chosen nickname contains invalid characters.");
    }
}

