package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

/**
 * Minstrel class is model representation of the Minstrel character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.MinstrelSwapStudents
 */
public class Minstrel extends CharacterCard {

    private GameState previousState;
    private int swappedStudents;
    public static final int maxSwaps = 2;

    /**
     * Constructor Minstrel sets the correct Character enum type and the correct price to the card.
     */
    public Minstrel(String imagePath) {
        super(imagePath);
        price = 1;
        character = Character.MINSTREL;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Minstrel logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.MINSTREL_SWAP_STUDENTS);
        swappedStudents = 0;
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Minstrel logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        if (game.getGameState().equals(GameState.MINSTREL_SWAP_STUDENTS)) {
            game.setGameState(previousState);
        }
    }

    /**
     * Method getSwappedStudents returns the number of student that have currently been swapped.
     */
    public int getSwappedStudents() {
        return swappedStudents;
    }

    /**
     * Method incrementSwapped increments the number of student swapped by one.
     */
    public void incrementSwapped() {
        swappedStudents++;
    }
}