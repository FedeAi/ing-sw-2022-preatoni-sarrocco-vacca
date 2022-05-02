package it.polimi.ingsw.Model;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.TowerColor;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class School implements Serializable {
    private int numTowers;
    private final TowerColor towerColor;

    private Map<Color, Integer> studentsHall;
    private Map<Color, Integer> studentsEntry;
    private final int laneSize = 9;

    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    public School(int numTowers, TowerColor towerColor, Map<Color, Integer> initialStudentsEntry) {
        this.numTowers = numTowers;
        this.towerColor = towerColor;
        this.studentsEntry = new EnumMap<Color, Integer>(initialStudentsEntry);
        studentsHall = new EnumMap<Color, Integer>(Color.class);
    }

    public void addPlayerListener(Player player){
        listener.addPropertyChangeListener(player);
    }
    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void decreaseTowers() {
        this.numTowers--;
        listener.firePropertyChange("", null, null);
    }

    public void increaseTowers() {
        this.numTowers++;
        listener.firePropertyChange("", null, null);
    }

    public int getNumTowers() {
        return numTowers;
    }

    public Map<Color, Integer> getStudentsEntry() {
        return new EnumMap<Color, Integer>(studentsEntry);
    }

    /**
     * It computes how many students there are in the Entry
     *
     * @return the number of overall students prensent in the Entry
     */
    public int getEntryStudentsNum() {
        return studentsEntry.values().stream().reduce(0, Integer::sum);
    }

    public void addStudentEntry(Color color) {
        studentsEntry.put(color, studentsEntry.getOrDefault(color, 0) + 1);
        listener.firePropertyChange("", null, null);
    }

    public void addStudentsEntry(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            studentsEntry.put(entry.getKey(), entry.getValue() + studentsEntry.getOrDefault(entry.getKey(), 0));
        }
        listener.firePropertyChange("", null, null);
    }

    public Map<Color, Integer> getStudentsHall() {
        return new EnumMap<Color, Integer>(studentsHall);
    }

    public void addStudentHall(Color color) {
        studentsHall.put(color, studentsHall.getOrDefault(color, 0) + 1);
        listener.firePropertyChange("", null, null);
    }

    public void addStudentsHall(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            studentsHall.put(entry.getKey(), entry.getValue() + studentsHall.getOrDefault(entry.getKey(), 0));
        }
        listener.firePropertyChange("", null, null);
    }

    public void moveStudentFromEntryToHall(Color student) {
        if (studentsEntry.get(student) > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
            studentsEntry.get(student);
            studentsHall.put(student, studentsHall.getOrDefault(student, 0) + 1);
        }
        listener.firePropertyChange("", null, null);
    }

    public void moveStudentFromHallToEntry(Color student) {
        if (studentsHall.get(student) > 0) {
            studentsHall.put(student, studentsHall.get(student) - 1);
            studentsEntry.put(student, studentsEntry.getOrDefault(student, 0) + 1);
        }
        listener.firePropertyChange("", null, null);
    }

    public void removeStudentFromEntry(Color student) {
        //boolean isRemoved = false;
        Integer numStudents = studentsEntry.get(student);
        if (numStudents != null && numStudents > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
        }
        listener.firePropertyChange("", null, null);
    }

    /**
     * SWAPPING STUDENTS FROM HALL TO ENTRY (FOR MINSTREL)
     */
    public void swapStudents(Color studentFromEntry, Color studentFromHall) {
        moveStudentFromEntryToHall(studentFromEntry);
        moveStudentFromHallToEntry(studentFromHall);
    }
}
