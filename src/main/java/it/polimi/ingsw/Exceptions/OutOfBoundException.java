package it.polimi.ingsw.Exceptions;

/**
 * @author Federico Sarrocco
 * @see Exception
 */
public class OutOfBoundException extends Exception {
    @Override
    public String getMessage(){
        return ("Error: out of bound");
    }
}

