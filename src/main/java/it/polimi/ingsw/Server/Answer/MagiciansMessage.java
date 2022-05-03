package it.polimi.ingsw.Server.Answer;

import it.polimi.ingsw.Constants.Magician;

import java.util.List;

/**
 * @author Federico Sarrocco
 */
public class MagiciansMessage implements Answer{
    private final String message;
    private List<Magician> remainingMagicians;

    public MagiciansMessage(String message){
        this.message = message;
    }

    public void addRemainingMagicians(List<Magician> magicians){
        remainingMagicians = magicians;
    }

    public List<Magician> getRemainingMagicians() {
        return remainingMagicians;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
