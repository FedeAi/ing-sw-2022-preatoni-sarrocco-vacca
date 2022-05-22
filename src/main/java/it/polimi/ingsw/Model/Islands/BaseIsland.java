package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Constants.Color;

import java.util.EnumMap;
import java.util.Map;

public class BaseIsland extends Island {

    private Map<Color, Integer> students;
    public BaseIsland() {
        super();
        students = new EnumMap<Color, Integer>(Color.class);
    }

    public BaseIsland(BaseIsland island) {
        super();
        students = island.students;
        isBlocked = island.isBlocked;
        owner = island.owner;
    }

    // TODO return a copy of the map, rather than the direct reference
    @Override
    public Map<Color, Integer> getStudents() {
        return new EnumMap<Color, Integer>(students);
    }

    // superIsland will override this method
    @Override
    public int getNumTower() {
        return owner != null ? 1 : 0;
    }

    @Override
    public void addStudent(Color student) {
        students.put(student, students.getOrDefault(student, 0) + 1);
    }


}
