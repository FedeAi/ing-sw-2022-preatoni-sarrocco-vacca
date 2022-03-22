package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Character;

public class CharacterCard extends Card {

    private int price;
    private Character character;

    public CharacterCard(String imagePath) {
        super(imagePath);
    }

    public int getPrice() {
        return price;
    }

    public Character getCharacter() {
        return character;
    }




}
