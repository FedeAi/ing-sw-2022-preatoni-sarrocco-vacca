package it.polimi.ingsw.Server.Answer;

import it.polimi.ingsw.Constants.Magician;

import java.util.List;

/**
 * @author Federico Sarrocco
 */
public class MagiciansMessage implements Answer{
    private final String message;
    private final List<Magician> remainingMagicians;

    public MagiciansMessage(String message, List<Magician> remainingMagicians){
        this.message = message;
        this.remainingMagicians = remainingMagicians;
    }

    public List<Magician> getRemainingMagicians() {
        return remainingMagicians;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
