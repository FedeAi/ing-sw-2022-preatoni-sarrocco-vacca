package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Monk class is model representation of the Monk character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.MonkMoveToIsland
 */
public class Monk extends CharacterCard {

    private Map<Color, Integer> students;
    private GameState previousState;
    private Bag bag;

    /**
     * Constructor Monk sets the correct Character enum type and the correct price to the card.
     */
    public Monk(Bag bag) {
        super();
        price = 1;
        this.bag = bag;
        students = new EnumMap<>(Color.class);
        character = Character.MONK;
    }

    /**
     * Method init overrides the default init behaviour of the CharacterCard abstract with the Monk logic.
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
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Monk logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.MONK_MOVE_STUDENT);
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Monk logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        if (game.getGameState().equals(GameState.MONK_MOVE_STUDENT)) {
            game.setGameState(previousState);
        }
    }

    /**
     * Method moveStudent removes the specified student from the card,
     * and then it refills it with a new one from the game bag.
     *
     * @param student the selected student from the card.
     */
    public void moveStudent(Color student) {
        if (students.get(student) != null) {
            students.put(student, students.get(student) - 1);
            // Refill the card after the use
            Color refill = bag.extract();
            students.put(refill, this.students.getOrDefault(student, 0) + 1);
        }
    }

    /**
     * Method getStudentsMap returns the card's student map.
     */
    public Map<Color, Integer> getStudentsMap() {
        return students;
    }

    /**
     * Method getStudentsMap returns the card's students.
     *
     * @return A list of students present on the card.
     */
    @Override
    public List<Color> getStudents() {
        return Color.fromMapToList(students);
    }
}