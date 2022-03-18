package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Island {

    private ArrayList<Student> students;
    private Tower tower;
    private Island nextIsland;
    private Player owner;

    public Island() {
        students = new ArrayList<Student>();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Player getOwner() {
        return owner;
    }

    public void addStudents(ArrayList<Student> students) {
        this.students.addAll(students);
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public void setNextIsland(Island nextIsland) {
        this.nextIsland = nextIsland;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
