package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Thief class is model representation of the Thief character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.ThiefChooseColor
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

    /**
     * Method getStudents returns a list of all colors, in order to make them selectable in the client.
     */
    @Override
    public List<Color> getStudents() {
        return new ArrayList<>(List.of(Color.values()));
    }
}