package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

/**
 * Grandma class is model representation of the Grandma character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.GrandmaBlockIsland
 */
public class Grandma extends CharacterCard {

    private int blockingCards;
    private GameState previousState;

    /**
     * Constructor Grandma sets the correct Character enum type and the correct price to the card.
     */
    public Grandma() {
        super();
        price = 2;
        blockingCards = 4;
        character = Character.GRANDMA;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Grandma logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Grandma logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        if (game.getGameState().equals(GameState.GRANDMA_BLOCK_ISLAND)) {
            game.setGameState(previousState);
        }
    }

    /**
     * Method removeBlockingCard removes a blocking card from the Grandma card.
     */
    public void removeBlockingCard() {
        if (blockingCards > 0) {
            blockingCards--;
        }
    }

    /**
     * Method addBlockingCard adds a blocking card to the Grandma card.
     */
    public void addBlockingCard() {
        if (blockingCards <= 4) {
            blockingCards++;
        }
    }

    /**
     * Method getBlockingCards returns the amount of blockingCards present on the Grandma card.
     */
    @Override
    public int getBlockingCards() {
        return blockingCards;
    }
}