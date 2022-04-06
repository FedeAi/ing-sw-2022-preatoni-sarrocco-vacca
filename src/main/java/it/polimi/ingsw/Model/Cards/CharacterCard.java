package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.DynamicRules;

public abstract class CharacterCard extends Card {

    private int price;
    private Character character;

    public CharacterCard(String imagePath) {
        super(imagePath);
    }


    public abstract void activate(Rules rules);
    public abstract void deactivate(Rules rules);
    public abstract boolean isActive();

    public int getPrice() {
        return price;
    }

    public Character getCharacter() {
        return character;
    }

}
