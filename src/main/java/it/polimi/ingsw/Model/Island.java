package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Island {

    ArrayList<Student> students;
    ArrayList<Tower> towers;
    Island nextIsland;
    Player Owner;


    public ArrayList<Student> getStudents() {
        return students;
    }
    //public
    public int getNumTowers(){
        return towers.size();
    }

    public Player getOwner() {
        return Owner;
    }
}
