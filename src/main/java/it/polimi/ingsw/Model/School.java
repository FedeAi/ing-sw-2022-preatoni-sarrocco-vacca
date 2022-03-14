package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class School {
    ArrayList<Tower> towers;
    ArrayList<Student> studentsHall;
    ArrayList<Student> studentsEntry;
    ArrayList<Professor> professors;
    final int laneSize = 9;

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
