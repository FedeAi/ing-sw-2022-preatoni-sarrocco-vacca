package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Islands.Island;

import java.util.EnumMap;
import java.util.Map;

public class BaseIsland implements Island {

    private Map<Color, Integer> students;
    private String owner;

    public BaseIsland() {
        students = new EnumMap<Color, Integer>(Color.class);
    }

    // TODO return a copy of the map, rather than the direct reference
    @Override
    public Map<Color, Integer> getStudents() {
        return students;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    // superIsland will override this method
    @Override
    public int getNumTower() {
        return owner != null ? 1 : 0;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void addStudent(Color student) {
        students.put(student, students.getOrDefault(student, 0) + 1);
    }


}