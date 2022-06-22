package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

/**
 * Thief class is model representation of the Thief character card.
 */
public class Thief extends CharacterCard {

    private GameState previousState;

    /**
     * Constructor Thief sets the correct Character enum type and the correct price to the card.
     */
    public Thief(String imagePath) {
        super(imagePath);
        price = 3;
        character = Character.THIEF;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Thief logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.THIEF_CHOOSE_COLOR);
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Thief logic.
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