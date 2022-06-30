package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * CharacterCard class represents the basic abstract of a character card.
 */
public abstract class CharacterCard implements Card {

    protected int price;
    protected boolean activated;
    protected boolean isActive;
    protected Character character;
    private String activatingPlayer;

    /**
     * Constructor CharacterCard initializes the parameter of the character card instance to the initial value.
     */
    public CharacterCard() {
        activated = false;
        isActive = false;
        activatingPlayer = "";
    }

    /**
     * Method init is responsible for the initialization of the internal state of a card.
     * This method is overrided as needed.
     */
    public void init() {
        return;
    }

    /**
     * Method activate is responsible for the basic card activation logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    public void activate(Rules rules, Game game) {
        activated = true;
        isActive = true;
        activatingPlayer = game.getRoundOwner().getNickname();
    }

    /**
     * Method deactivate is responsible for the basic card deactivation logic.
     *
     * @param rules the current rules of the game
     * @param game  the reference to the current game
     */
    public void deactivate(Rules rules, Game game) {
        isActive = false;
    }

    /**
     * Method isActive returns if the card is currently active
     * @return true if the card is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Method alreadyActivatedOnce returns if the card has been activated once.
     * @return true if the card has been activated at least once, false otherwise
     */
    public boolean alreadyActivatedOnce() {
        return activated;
    }

    /**
     * Method getPrice returns the price of the card, incremented if needed.
     * @return the price of the card if it has not been activated once,
     * the price of the card incremented by one otherwise.
     */
    public int getPrice() {
        if (activated) {
            return price + 1;
        }
        return price;
    }

    /**
     * Method getCharacter returns the type of the character card.
     * @see Character
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Method resetActivatingPlayer resets the card's activator.
     */
    public void resetActivatingPlayer() {
        activatingPlayer = "";
    }

    /**
     * Method getActivatingPlayer returns the player that has activated the card at the moment.
     * @return the username of the player.
     */
    public String getActivatingPlayer() {
        return activatingPlayer;
    }

    /**
     * Method getStudents returns the list of the students present on the card.
     */
    public List<Color> getStudents() {
        return new ArrayList<>();
    }

    /**
     * Method getBlockingCards returns the number of blocking cards present on the character card.
     * The returned value will be 0 by default and overrided as needed.
     */
    public int getBlockingCards() {
        return 0;
    }
}
