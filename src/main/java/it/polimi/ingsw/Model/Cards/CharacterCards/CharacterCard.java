package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Enumerations.Character;
import it.polimi.ingsw.Model.Game;

public abstract class CharacterCard extends Card {

    protected int price;
    protected boolean activated;
    protected boolean isActive;
    private Character character;

    public CharacterCard(String imagePath) {
        super(imagePath);
        activated = false;
    }

    public abstract void activate(Rules rules, Game game);

    public abstract void deactivate(Rules rules, Game game);

    public boolean isActive() {
        return isActive;
    }

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
