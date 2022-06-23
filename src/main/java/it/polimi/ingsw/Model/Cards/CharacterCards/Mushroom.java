package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.MushroomRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Mushroom class is model representation of the Mushroom character card.
 *
 * @see it.polimi.ingsw.Controller.Actions.CharacterActions.MushroomChooseColor
 */
public class Mushroom extends CharacterCard {

    private Color student;
    private GameState previousState;

    /**
     * Constructor Mushroom sets the correct Character enum type and the correct price to the card.
     */
    public Mushroom(String imagePath) {
        super(imagePath);
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
        game.setGameState(previousState);
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