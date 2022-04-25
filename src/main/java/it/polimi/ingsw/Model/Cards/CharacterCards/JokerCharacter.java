package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.EnumMap;
import java.util.Map;

public class JokerCharacter extends CharacterCard {

    private Map<Color, Integer> students;
    private GameState previousState;
    private int swappedStudents;
    private Bag bag;

    public static final int maxSwaps = 3;

    public JokerCharacter(String imagePath, Bag bag) {
        super(imagePath);
        price = 1;

        swappedStudents = 0;
        this.bag = bag;
        // students map
        this.students = new EnumMap<Color, Integer>(Color.class);
    }

    @Override
    public void init() {
        for (int i=0; i<6;i++) {
            Color student = bag.extractOne();
            this.students.put(student, this.students.getOrDefault(student, 0) + 1);
        }
    }


    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        previousState = game.getGameState();
        game.setGameState(GameState.JOKER_SWAP_STUDENTS);
        swappedStudents = 0;

    }

    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        game.setGameState(previousState);
    }

    /**
     * @param studentToPick the student you pick from the card
     * @param studentToPut  the student you put on the card
     */
    public void swapStudents(Color studentToPick, Color studentToPut) throws IllegalArgumentException {
        if (students.getOrDefault(studentToPick, 0) <= 0) {
            throw new IllegalArgumentException("the student you choose to pick is not in there");
        } else {
            students.put(studentToPut, students.getOrDefault(studentToPut, 0) + 1);
            students.put(studentToPut, students.get(studentToPick) - 1);
            swappedStudents++;
        }
    }

    public int getSwappedStudents() {
        return swappedStudents;
    }

    public Map<Color, Integer> getStudents() {
        return students;
    }
}
