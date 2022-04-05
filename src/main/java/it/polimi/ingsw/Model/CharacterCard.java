package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Character;

public class CharacterCard extends Card {

    private int price;
    private boolean active;
    private Character character;

    public CharacterCard(String imagePath) {
        super(imagePath);
        active = false;
    }

    public int getPrice() {
        return price;
    }

    public Character getCharacter() {
        return character;
    }

    // TODO CODE THE ACTIVATE() AND DEACTIVATE() METHODS (STRATEGY PATTERN)

}
