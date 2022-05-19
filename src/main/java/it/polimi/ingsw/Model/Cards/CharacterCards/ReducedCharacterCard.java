package it.polimi.ingsw.Model.Cards.CharacterCards;

import java.io.Serializable;

public class ReducedCharacterCard implements Serializable {
    public final boolean isActive;
    public final String name;
    public final int price;

    public ReducedCharacterCard(boolean isActive, String name, int price) {
        this.isActive = isActive;
        this.name = name;
        this.price = price;
    }
}
