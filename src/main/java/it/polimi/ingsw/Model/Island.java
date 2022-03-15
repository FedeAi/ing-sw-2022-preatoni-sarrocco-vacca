package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Island {

    private ArrayList<Student> students;
    private ArrayList<Tower> towers;
    private Island nextIsland;
    private Player owner;


    public ArrayList<Student> getStudents() {
        return students;
    }
    //public
    public int getNumTowers(){
        return towers.size();
    }

    public Player getOwner() {
        return owner;
    }
}
