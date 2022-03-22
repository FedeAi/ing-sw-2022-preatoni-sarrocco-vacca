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

    // setTower and setOwner are inherently connected, if someone can put a tower on a certain island, we are sure he is the owner.
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
