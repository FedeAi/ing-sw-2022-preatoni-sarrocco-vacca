package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.TowerColor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Island {

    private Map<Color, Integer> students;
    private TowerColor tower;
    private Player owner;

    public Island() {
        students = new EnumMap<Color, Integer>(Color.class);
    }

    public Map<Color, Integer> getStudents() {
        return students;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void addStudent(Color student) {
        students.put(student, students.getOrDefault(student, 0) + 1);
    }

    // TODO setTower and setOwner are inherently connected, if someone can put a tower on a certain island, we are sure he is the owner.
    public void setTower(TowerColor tower) {
        this.tower = tower;
    }

}
