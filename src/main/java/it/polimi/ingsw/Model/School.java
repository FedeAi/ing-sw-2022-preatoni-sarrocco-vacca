package it.polimi.ingsw.Model;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.TowerColor;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

/**
 * School class represents the game's school boards, which contain the students' hall and entry and the player's towers.
 */
public class School implements Serializable {

    private int numTowers;
    private final TowerColor towerColor;
    private Map<Color, Integer> studentsHall;
    private Map<Color, Integer> studentsEntry;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    /**
     * Constructor School creates a School instance with the initial values.
     *
     * @param numTowers            the initial number of towers.
     * @param towerColor           the player's tower color.
     * @param initialStudentsEntry the initial students' entry.
     */
    public School(int numTowers, TowerColor towerColor, Map<Color, Integer> initialStudentsEntry) {
        this.numTowers = numTowers;
        this.towerColor = towerColor;
        this.studentsEntry = new EnumMap<>(initialStudentsEntry);
        studentsHall = new EnumMap<>(Color.class);
    }

    /**
     * Method addPlayerListener adds a listener to the specified player
     *
     * @param player the reference to the player.
     */
    public void addPlayerListener(Player player) {
        listener.addPropertyChangeListener(player);
    }

    /**
     * Method getTowerColor returns the school's tower color.
     *
     * @see TowerColor
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * Method decreaseTowers removes a tower after an island conquer by a player, and fires the changes to the clients.
     */
    public void decreaseTowers() {
        this.numTowers--;
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method increaseTowers increases the tower count by one
     * after a previously conquered island get conquered by another player, and fires the changes to the clients.
     */
    public void increaseTowers() {
        this.numTowers++;
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method getNumTowers returns the number of towers present on the school.
     */
    public int getNumTowers() {
        return numTowers;
    }

    /**
     * Method getStudentsEntry returns the students' entry map present on the school.
     *
     * @return The copy of the entry map.
     */
    public Map<Color, Integer> getStudentsEntry() {
        return new EnumMap<>(studentsEntry);
    }

    /**
     * Method getStudentsEntryList returns the students' entry list present on the school.
     *
     * @return A list of students created from the map.
     */
    public List<Color> getStudentsEntryList() {
        ArrayList<Color> out = new ArrayList<>();
        for (Map.Entry<Color, Integer> entry : studentsEntry.entrySet()) {
            out.addAll(Collections.nCopies(entry.getValue(), entry.getKey()));
        }
        return out;
    }

    /**
     * Method getEntryStudentsNum returns the number of students presents in the entry.
     */
    public int getEntryStudentsNum() {
        return studentsEntry.values().stream().reduce(0, Integer::sum);
    }

    /**
     * Method addStudentEntry adds a single specified student to the entry student map.
     *
     * @param color the specified student's color.
     */
    public void addStudentEntry(Color color) {
        studentsEntry.put(color, studentsEntry.getOrDefault(color, 0) + 1);
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method addStudentEntry adds a map of students to the entry student map.
     *
     * @param addStudents the map of students to be added to the entry.
     */
    public void addStudentsEntry(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            studentsEntry.put(entry.getKey(), entry.getValue() + studentsEntry.getOrDefault(entry.getKey(), 0));
        }
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method getStudentsHall returns the map of the school's hall.
     *
     * @return the copy of the school's hall.
     */
    public Map<Color, Integer> getStudentsHall() {
        return new EnumMap<Color, Integer>(studentsHall);
    }

    /**
     * Method getStudentsHallList returns the list of the school's hall.
     *
     * @return the list of the school's hall.
     */
    public List<Color> getStudentsHallList() {
        ArrayList<Color> out = new ArrayList<>();
        for (Map.Entry<Color, Integer> entry : studentsHall.entrySet()) {
            out.addAll(Collections.nCopies(entry.getValue(), entry.getKey()));
        }
        return out;
    }

    /**
     * Method addStudentHall adds a single student to the school's hall.
     *
     * @param color the student to be added to the hall map.
     */
    public void addStudentHall(Color color) {
        studentsHall.put(color, studentsHall.getOrDefault(color, 0) + 1);
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method addStudentHall adds a map of students to the school's hall.
     *
     * @param addStudents the student map to be added.
     */
    public void addStudentsHall(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            studentsHall.put(entry.getKey(), entry.getValue() + studentsHall.getOrDefault(entry.getKey(), 0));
        }
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method moveStudentFromEntryToHall moves a single student from the school's entry to the hall.
     *
     * @param student the student to be moved.
     * @see it.polimi.ingsw.Controller.Actions.MoveStudentFromEntryToHall
     */
    public void moveStudentFromEntryToHall(Color student) {
        if (studentsEntry.get(student) > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
            studentsEntry.get(student);
            studentsHall.put(student, studentsHall.getOrDefault(student, 0) + 1);
        }
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method moveStudentFromHallToEntry moves a single student from the school's hall to the entry.
     *
     * @param student the student to be moved.
     * @see School#swapStudents(Color, Color)
     */
    public void moveStudentFromHallToEntry(Color student) {
        if (studentsHall.get(student) > 0) {
            studentsHall.put(student, studentsHall.get(student) - 1);
            studentsEntry.put(student, studentsEntry.getOrDefault(student, 0) + 1);
        }
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method removeStudentFromEntry removes a single student from the school's entry.
     *
     * @param student the student to be removed.
     */
    public void removeStudentFromEntry(Color student) {
        Integer numStudents = studentsEntry.get(student);
        if (numStudents != null && numStudents > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
        }
        listener.firePropertyChange("", null, null);
    }

    /**
     * Method swapStudents manages the logic of the Minstrel character card,
     * which allows the swap of a student from the hall to the entry and vice versa.
     *
     * @param studentFromEntry the color of the student from the entry to the hall.
     * @param studentFromHall the color of the student from the hall to the entry.
     * @see it.polimi.ingsw.Controller.Actions.CharacterActions.MinstrelSwapStudents
     */
    public void swapStudents(Color studentFromEntry, Color studentFromHall) {
        moveStudentFromEntryToHall(studentFromEntry);
        moveStudentFromHallToEntry(studentFromHall);
    }
}
