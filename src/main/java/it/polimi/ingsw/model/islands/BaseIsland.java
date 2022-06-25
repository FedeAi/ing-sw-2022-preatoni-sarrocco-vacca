package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.constants.Color;

import java.util.EnumMap;
import java.util.Map;

/**
 * BaseIsland class represent the normal game island. It contains a map of students.
 */
public class BaseIsland extends Island {

    private Map<Color, Integer> students;

    /**
     * Constructor BaseIsland extends the underlying Island abstract by initializing the student map.
     *
     * @see Island
     */
    public BaseIsland() {
        super();
        students = new EnumMap<Color, Integer>(Color.class);
    }

    /**
     * Constructor BaseIsland creates a copy of the provided BaseIsland.
     *
     * @param island the BaseIsland to be copied.
     * @see Island
     */
    public BaseIsland(BaseIsland island) {
        super();
        students = island.students;
        isBlocked = island.isBlocked;
        owner = island.owner;
    }

    /**
     * Method getStudents returns the copy of the BaseIsland student map.
     */
    @Override
    public Map<Color, Integer> getStudents() {
        return new EnumMap<>(students);
    }

    /**
     * Method getNumTowers returns the number of towers present on the BaseIsland.
     *
     * @return 1 if the tower is present, 0 otherwise.
     */
    @Override
    public int getNumTower() {
        return owner != null ? 1 : 0;
    }

    /**
     * Method addStudent adds a student to the BaseIsland.
     *
     * @param student the provided student to be put on the BaseIsland.
     */
    @Override
    public void addStudent(Color student) {
        students.put(student, students.getOrDefault(student, 0) + 1);
    }
}