package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.EnumMap;
import java.util.Map;

public class BaseIsland implements Island {

    private Map<Color, Integer> students;
    private String owner;

    public BaseIsland() {
        students = new EnumMap<Color, Integer>(Color.class);
    }

    //TODO override
    public Map<Color, Integer> getStudents() {
        return students;    // TODO return new Object ( immutable class for getters)
    }

    public String getOwner() {
        return owner;
    }

    public int getNumTower(){       // superIsland will override this method
        return owner!=null ? 1 : 0;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void addStudent(Color student) {
        students.put(student, students.getOrDefault(student, 0) + 1);
    }


}
