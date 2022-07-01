package it.polimi.ingsw.exceptions;

/**
 * @see Exception
 */
public class OutOfBoundException extends Exception {
    @Override
    public String getMessage(){
        return ("Error: out of bound");
    }
}

