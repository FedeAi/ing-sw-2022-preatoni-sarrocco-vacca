package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Model.Game;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterCard extends Card {

    protected int price;
    protected boolean activated;
    protected boolean isActive;
    protected Character character;
    private String activatingPlayer;

    public CharacterCard(String imagePath) {
        super(imagePath);
        activated = false;
        isActive = false;
        activatingPlayer = "";
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
        activatingPlayer = game.getRoundOwner().getNickname();
    }

    public void deactivate(Rules rules, Game game) {
        isActive = false;
        activatingPlayer = "";
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

    public String getActivatingPlayer() {
        return activatingPlayer;
    }

    /**
     * @return the professors on the card
     */
    public List<Color> getStudents() {
        return new ArrayList<>();
    }

    /**
     * @return the number of blocking cards on the card
     */
    public int getBlockingCards() {
        return 0;
    }
}
