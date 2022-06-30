package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.actions.characters.PrincessMoveToHall;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Princess class is model representation of the Princess character card.
 *
 * @see PrincessMoveToHall
 */
public class Princess extends CharacterCard {

    private Map<Color, Integer> students;
    private GameState previousState;
    private Bag bag;

    /**
     * Constructor Princess sets the correct Character enum type and the correct price to the card.
     *
     * @param bag this is the game bag reference, needed to maintain the card's student inventory.
     */
    public Princess(Bag bag) {
        super();
        price = 2;
        this.bag = bag;
        students = new EnumMap<>(Color.class);
        character = Character.PRINCESS;
    }

    /**
     * Method init overrides the default init behaviour of the CharacterCard abstract with the Princess logic.
     * The method fills the Monk student map with extracted students from the Bag.
     */
    @Override
    public void init() {
        for (int i = 0; i < 4; i++) {
            Color student = bag.extract();
            this.students.put(student, this.students.getOrDefault(student, 0) + 1);
        }
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Princess logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.PRINCESS_MOVE_STUDENT);

    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Princess logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        if (game.getGameState().equals(GameState.PRINCESS_MOVE_STUDENT)) {
            game.setGameState(previousState);
        }
    }

    /**
     * Method moveStudent removes a student from the card's student map and returns it
     * (to be added to the player's hall).
     *
     * @param student the selected student's color.
     */
    public void moveStudent(Color student) {
        if (students.get(student) != null) {
            students.put(student, students.get(student) - 1);
            Color refill = bag.extract();
            students.put(refill, this.students.getOrDefault(refill, 0) + 1);
        }
    }

    /**
     * Method getStudentsMap returns the reference to the card's students map
     */
    public Map<Color, Integer> getStudentsMap() {
        return students;
    }

    /**
     * Method getStudents returns the list of the card's students map.
     */
    @Override
    public List<Color> getStudents() {
        return Color.fromMapToList(students);
    }
}
