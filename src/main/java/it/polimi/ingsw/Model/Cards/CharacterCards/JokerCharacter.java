package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class JokerCharacter extends CharacterCard {
    private Map<Color, Integer> students;
    private GameState previousState;
    private int swappedStudents;
    public static final int maxSwaps = 3;

    public JokerCharacter(String imagePath, ArrayList<Color> students) {
        super(imagePath);
        swappedStudents = 0;
        if (students.size() != 6) {
            throw new IllegalArgumentException("size of students must me 6");
        }

        // fill students map
        this.students = new EnumMap<Color, Integer>(Color.class);
        for (Color stud : students) {
            this.students.put(stud, this.students.getOrDefault(stud, 0) + 1);
        }

        price = 1;
        isActive = false;
        activated = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        previousState = game.getGameState();
        game.setGameState(GameState.JOKER_SWAP_STUDENTS);
        swappedStudents = 0;

    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        game.setGameState(previousState);
    }

    /**
     * @param studentToPick the student you pick from the island
     * @param studentToPut  the student you put on the island
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
