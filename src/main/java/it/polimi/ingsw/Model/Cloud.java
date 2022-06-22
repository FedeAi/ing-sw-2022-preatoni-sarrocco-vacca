package it.polimi.ingsw.Model;

import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Cloud class represent the clouds to be selected at the end of the turn to refill the player's school.
 */
public class Cloud implements Serializable {

    /**
     * The students on the cloud
     */
    private Map<Color, Integer> students;

    /**
     * Constructor Cloud creates the map of the students on the cloud.
     */
    public Cloud() {
        students = new EnumMap<Color, Integer>(Color.class);
    }

    /**
     * Method pickStudents replaces the students' map, effectively removing them.
     * @return The map of the students on the cloud before removal.
     */
    public Map<Color, Integer> pickStudents() {
        Map<Color, Integer> returnStudents = students;
        students = new EnumMap<>(Color.class);
        return returnStudents;
    }

    /**
     * Method isEmpty checks the emptiness of a cloud.
     * @return true if the map is new, false otherwise.
     */
    public boolean isEmpty() {
        return students.size() == 0;
    }

    /**
     * Method addStudents adds the students to an empty cloud.
     * @param addStudents the map to be added to the cloud's students.
     */
    public void addStudents(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            students.put(entry.getKey(), entry.getValue() + students.getOrDefault(entry.getKey(), 0));
        }
    }

    /**
     * Method getStudents returns the students on the cloud.
     * @return A copy of the student map.
     */
    public Map<Color, Integer> getStudents() {
        return new EnumMap<>(students);
    }
}
