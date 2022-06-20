package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReducedCharacterCard implements Serializable {
    public final boolean isActive;
    public final Character type;
    public final int price;
    public final List<Color> students;
    public final int blockingCards;

    public ReducedCharacterCard(CharacterCard characterCard) {
        this.isActive = characterCard.isActive();
        this.type = characterCard.getCharacter();
        this.price = characterCard.getPrice();
        this.students = characterCard.getStudents();
        this.blockingCards = characterCard.getBlockingCards();
    }
}
