package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Character;
import it.polimi.ingsw.Model.Game;

public abstract class CharacterCard extends Card {

    protected int price;
    protected boolean activated;
    private Character character;

    public CharacterCard(String imagePath) {
        super(imagePath);
        activated = false;
    }

    public abstract void activate(Rules rules);

    public abstract void activate(Rules rules, Game game);
    public abstract void deactivate(Rules rules);

    public abstract boolean isActive();

    public int getPrice() {
        if (activated) {
            return price + 1;
        }
        return price;
    }

    public Character getCharacter() {
        return character;
    }

}
