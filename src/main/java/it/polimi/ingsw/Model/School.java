package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.TowerColor;

import java.util.EnumMap;
import java.util.Map;

public class School {
    private int numTowers;
    private final TowerColor towerColor;

    private Map<Color, Integer> studentsHall;
    private Map<Color, Integer> studentsEntry;
    private final int laneSize = 9;

    public School(int numTowers, TowerColor towerColor, Map<Color, Integer> initialStudentsEntry) {
        this.numTowers = numTowers;
        this.towerColor = towerColor;
        this.studentsEntry = new EnumMap<Color, Integer>(initialStudentsEntry);
        studentsHall = new EnumMap<Color, Integer>(Color.class);
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void decreaseTowers() {
        this.numTowers--;
    }

    public void increaseTowers() {
        this.numTowers++;
    }

    public int getNumTowers() {
        return numTowers;
    }

    public Map<Color, Integer> getStudentsEntry() {
        return studentsEntry;
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
    }

    public void addStudentsEntry(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            studentsEntry.put(entry.getKey(), entry.getValue() + studentsEntry.getOrDefault(entry.getKey(), 0));
        }
    }

    public Map<Color, Integer> getStudentsHall() {
        return studentsHall;
    }

    public void addStudentHall(Color color) {
        studentsHall.put(color, studentsHall.getOrDefault(color, 0) + 1);
    }

    public void addStudentsHall(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            studentsHall.put(entry.getKey(), entry.getValue() + studentsHall.getOrDefault(entry.getKey(), 0));
        }
    }

    public void moveStudentFromEntryToHall(Color student) {
        if (studentsEntry.get(student) > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
            studentsHall.put(student, studentsHall.getOrDefault(student, 0) + 1);
        }
    }

    public void removeStudentFromEntry(Color student) {
        //boolean isRemoved = false;
        Integer numStudents = studentsEntry.get(student);
        if (numStudents != null && numStudents > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
            //isRemoved = true;
        }
        //return isRemoved;
    }
}
