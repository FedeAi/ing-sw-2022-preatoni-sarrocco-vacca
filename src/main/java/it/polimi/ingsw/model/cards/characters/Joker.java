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
 * Joker class is model representation of the Joker character card.
 *
 * @see it.polimi.ingsw.controller.actions.characters.JokerSwapStudents
 */
public class Joker extends CharacterCard {

    private Map<Color, Integer> students;
    private GameState previousState;
    private int swappedStudents;
    private Bag bag;
    public static final int maxSwaps = 3;

    /**
     * Constructor Joker sets the correct Character enum type and the correct price to the card.
     */
    public Joker(Bag bag) {
        super();
        price = 1;
        swappedStudents = 0;
        this.bag = bag;
        // students map
        this.students = new EnumMap<>(Color.class);
        character = Character.JOKER;
    }

    /**
     * Method init overrides the default init behaviour of the CharacterCard abstract with the Joker logic.
     * The method fills the Joker student list with extracted students from the Bag.
     */
    @Override
    public void init() {
        for (int i = 0; i < 6; i++) {
            Color student = bag.extract();
            this.students.put(student, this.students.getOrDefault(student, 0) + 1);
        }
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Joker logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.JOKER_SWAP_STUDENTS);
        swappedStudents = 0;
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Joker logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        if (game.getGameState().equals(GameState.JOKER_SWAP_STUDENTS)) {
            game.setGameState(previousState);
        }
    }

    /**
     * Method swapStudents swaps students from the card's student map.
     *
     * @param studentToPick the student you pick from the card
     * @param studentToPut  the student you put on the card
     */
    public void swapStudents(Color studentToPick, Color studentToPut) throws IllegalArgumentException {
        students.put(studentToPut, students.getOrDefault(studentToPut, 0) + 1);
        students.put(studentToPut, students.get(studentToPick) - 1);
        swappedStudents++;
    }

    /**
     * Method getSwappedStudents returns the number of students currently swapped.
     */
    public int getSwappedStudents() {
        return swappedStudents;
    }

    /**
     * Method getStudentsMap returns the map of the card's students.
     */
    public Map<Color, Integer> getStudentsMap() {
        return students;
    }

    /**
     * Method getStudents overrides the default behaviour of the CharacterCard abstract.
     *
     * @return A student list built from the card's student map.
     */
    @Override
    public List<Color> getStudents() {
        return Color.fromMapToList(students);
    }
}