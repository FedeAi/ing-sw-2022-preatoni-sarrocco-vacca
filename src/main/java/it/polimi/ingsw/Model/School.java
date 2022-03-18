package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.ArrayList;

public class School {
    private ArrayList<Tower> towers;
    private ArrayList<Student> studentsHall;
    private ArrayList<Student> studentsEntry;
    private ArrayList<Professor> professors;
    private final int laneSize = 9;

    public int getNumTowers(){
        return towers.size();
    }

    public int getNumStudentsHall(Color color) {
        assert false;
        return 0;
    }

    public ArrayList<Student> getStudentsEntry() {
        return studentsEntry;
    }

    public boolean hasProfessor(Color color){
        assert false;
        return false;
    }


}
