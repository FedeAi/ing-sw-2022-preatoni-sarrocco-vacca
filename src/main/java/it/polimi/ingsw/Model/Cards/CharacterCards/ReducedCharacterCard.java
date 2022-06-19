package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReducedCharacterCard implements Serializable {
    public final boolean isActive;
    public final String name;
    public final int price;
    public final List<Color> students;
    public final int blockingCards;

    public ReducedCharacterCard(CharacterCard characterCard) {
        this.isActive = characterCard.isActive();
        this.name = characterCard.getClass().getSimpleName();
        this.price = characterCard.getPrice();
        this.students = characterCard.getStudents();
        this.blockingCards = characterCard.getBlockingCards();
    }

    /**
     * if the card has students on it, this method returns it
     * @return
     */
    public List<Color> getStudents(){
        return new ArrayList<>(students);
    }

    /**
     * if the card has blocking cards on it, this method returns it
     * @return
     */
    public int numBlockingCard(){
        return blockingCards;
    }
}
