package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.constants.Color;

import java.io.Serializable;
import java.util.List;

/**
 * ReducedCharacterCard class is a generic representation of the CharacterCard abstract to be sent to client,
 * removing unnecessary information.
 */
public class ReducedCharacterCard implements Serializable {
    public final boolean isActive;
    public final Character type;
    public final int price;
    public final List<Color> students;
    public final int blockingCards;
    public final boolean activatedOnce;

    /**
     * Constructor ReducedCharacterCard creates an RCC instance based on the CharacterCard parameter.
     * This represents a Serializable version of a CC to be sent to the client.
     *
     * @param characterCard the card to be forwarded to the client.
     */

    public ReducedCharacterCard(CharacterCard characterCard) {
        this.isActive = characterCard.isActive();
        this.type = characterCard.getCharacter();
        this.price = characterCard.getPrice();
        this.students = characterCard.getStudents();
        this.blockingCards = characterCard.getBlockingCards();
        this.activatedOnce = characterCard.alreadyActivatedOnce();
    }
}