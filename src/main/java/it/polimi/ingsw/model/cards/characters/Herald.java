package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

/**
 * Herald class is model representation of the Herald character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.HeraldChooseIsland
 */
public class Herald extends CharacterCard {

    private GameState previousState;

    /**
     * Constructor Herald sets the correct Character enum type and the correct price to the card.
     */
    public Herald() {
        super();
        price = 3;
        character = Character.HERALD;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Herald logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.HERALD_ACTIVE);
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Herald logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        game.setGameState(previousState);
    }
}