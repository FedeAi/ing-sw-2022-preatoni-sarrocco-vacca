package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class School {
    private int numTowers;
    private final TowerColor towerColor;

    private Map<Color, Integer> studentsHall;
    private Map<Color, Integer> studentsEntry;
    private final int laneSize = 9;

    public School(int numTowers,TowerColor towerColor , Map<Color, Integer> initialStudentsEntry){
        this.numTowers = numTowers;
        this.studentsEntry = initialStudentsEntry;
        this.towerColor = towerColor;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }
    public void decreseTowers(){
        this.numTowers--;
    }
    public void increseTowers(){
        this.numTowers++;
    }
    public int getNumTowers() {
        return numTowers;
    }

    public Map<Color, Integer> getStudentsEntry() {
        return studentsEntry;
    }

    public void addStudentEntry(Color color) {
        studentsEntry.put(color, studentsEntry.getOrDefault(color, 0) + 1);
    }

    public Map<Color, Integer> getStudentsHall() {
        return studentsHall;
    }

    public void addStudentHall(Color color) {
        studentsHall.put(color, studentsEntry.getOrDefault(color, 0) + 1);
    }

    public void moveStudentFromEntryToHall(Color student){
        if(studentsEntry.get(student) > 0){
            studentsEntry.put(student, studentsEntry.get(student)-1);
            studentsHall.put(student, studentsEntry.getOrDefault(student,0)+1);
        }
    }


    // TODO TESTING
    public boolean removeStudentFromEntry(Color student) {
        boolean isRemoved = false;
        Integer numStudents  = studentsEntry.get(student);
        if(numStudents != null && numStudents > 0) {
            studentsEntry.put(student, studentsEntry.get(student) - 1);
            isRemoved = true;
        }
        return isRemoved;
    }
}
