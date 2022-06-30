package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.dynamic.BaseRules;
import it.polimi.ingsw.controller.rules.dynamic.MushroomRules;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Mushroom class is model representation of the Mushroom character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.MushroomChooseColor
 */
public class Mushroom extends CharacterCard {

    private Color student;
    private GameState previousState;

    /**
     * Constructor Mushroom sets the correct Character enum type and the correct price to the card.
     */
    public Mushroom() {
        super();
        price = 3;
        character = Character.MUSHROOM;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Mushroom logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.MUSHROOM_CHOOSE_COLOR);
        rules.setDynamicRules(new MushroomRules());
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Mushroom logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        if (game.getGameState().equals(GameState.MUSHROOM_CHOOSE_COLOR)) {
            game.setGameState(previousState);
        }
        rules.setDynamicRules(new BaseRules());
        setStudent(null);
    }

    /**
     * Method getStudent returns the currently chosen student.
     */
    public Color getStudent() {
        return student;
    }

    /**
     * Method setStudent allows the player that activates the card to set a student on the card.
     * The selected student's color will not count towards influence calculation.
     *
     * @param student the student to be set.
     */
    public void setStudent(Color student) {
        this.student = student;
    }

    /**
     * Method getPreviousState returns the state previous to the Mushroom activation.
     */
    public GameState getPreviousState() {
        return previousState;
    }

    /**
     * Method getStudents returns a list of all colors, in order to make them selectable in the client.
     */
    @Override
    public List<Color> getStudents() {
        return new ArrayList<>(List.of(Color.values()));
    }
}