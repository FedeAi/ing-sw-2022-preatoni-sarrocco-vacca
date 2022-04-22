package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SuperIsland extends Island {
    private String owner;
    List<BaseIsland> islands;

    public SuperIsland(List<Island> islands) {
        this.islands = new ArrayList<>();
        for (Island island : islands) {
            if (island instanceof SuperIsland) {
                this.islands.addAll(((SuperIsland) island).islands);
            } else if (island instanceof BaseIsland) {
                this.islands.add((BaseIsland) island);
            }
            owner = owner == null ? island.getOwner() : owner;
        }

    }

    List<BaseIsland> getBaseIslands() {
        return islands;
    }

    @Override
    public Map<Color, Integer> getStudents() {
        Map<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        for (Island i : islands) {
            for (Map.Entry<Color, Integer> entry : i.getStudents().entrySet()) {
                students.put(entry.getKey(), entry.getValue() + students.getOrDefault(entry.getKey(), 0));
            }
        }
        return students;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public int getNumTower() {
        return islands.stream().reduce(0, (numTowers, island) -> numTowers + island.getNumTower(), Integer::sum);
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
        for (Island island : islands) {
            island.setOwner(owner);
        }
    }

    @Override
    public void addStudent(Color student) {
        // we decided to add the student to the first island of the super island group
        islands.get(0).addStudent(student);
    }
}
