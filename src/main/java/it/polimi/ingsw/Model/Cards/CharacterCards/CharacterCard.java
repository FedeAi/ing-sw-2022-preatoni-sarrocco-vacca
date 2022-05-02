package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Model.Game;

public abstract class CharacterCard extends Card {

    protected int price;
    protected boolean activated;
    protected boolean isActive;
    private Character character;

    public CharacterCard(String imagePath) {
        super(imagePath);
        activated = false;
        isActive = false;
    }

    /**
     * this method is called to initialized internal state of the card, as the constructor will not do that
     */
    public void init() {
        return;
    }

    public void activate(Rules rules, Game game) {
        activated = true;
        isActive = true;
    }

    public void deactivate(Rules rules, Game game) {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean alreadyActivatedOnce() {
        return activated;
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
