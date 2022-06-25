package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.constants.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * SuperIsland class is the representation of multiple island that have been merged, as required by the game's rules.
 */
public class SuperIsland extends Island {
    List<BaseIsland> islands;

    /**
     * Constructor SuperIsland creates and adds the merged islands to the SuperIslands islands list.
     *
     * @param islands the list of islands to be merged.
     */
    public SuperIsland(List<Island> islands) {
        super();
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

    /**
     * Constructor SuperIslands returns a copied instance of the provided SuperIsland.
     *
     * @param island the SuperIsland instance to be copied.
     */
    public SuperIsland(SuperIsland island) {
        super();
        islands = island.islands;
        owner = island.owner;
        isBlocked = island.isBlocked;
    }

    /**
     * Method getBaseIslands returns list of the merged islands that the SuperIsland is composed of.
     */
    public List<BaseIsland> getBaseIslands() {
        return islands;
    }

    /**
     * Method getStudents returns the students present on a SuperIsland,
     * by making the sum of all the merged island's students.
     */
    @Override
    public Map<Color, Integer> getStudents() {
        Map<Color, Integer> students = new EnumMap<>(Color.class);
        for (Island i : islands) {
            for (Map.Entry<Color, Integer> entry : i.getStudents().entrySet()) {
                students.put(entry.getKey(), entry.getValue() + students.getOrDefault(entry.getKey(), 0));
            }
        }
        return students;
    }

    /**
     * Method getNumTowers returns the number of towers of the SuperIsland.
     */
    @Override
    public int getNumTower() {
        return islands.stream().reduce(0, (numTowers, island) -> numTowers + island.getNumTower(), Integer::sum);
    }

    /**
     * Method setOwner sets the ownership of the SuperIsland,
     * and also to all the merged BaseIslands of which the SuperIsland is composed of.
     *
     * @param owner the nickname of the owner to be set.
     */
    @Override
    public void setOwner(String owner) {
        this.owner = owner;
        for (BaseIsland island : islands) {
            island.setOwner(owner);
        }
    }

    /**
     * Method addStudent adds a student to a SuperIsland.
     * @param student the student's color to be added to the island.
     */
    @Override
    public void addStudent(Color student) {
        // we decided to add the student to the first island of the super island group
        islands.get(0).addStudent(student);
    }

    /**
     * Method size returns the number of merged islands that the SuperIsland is composed of.
     */
    @Override
    public int size() {
        return islands.size();
    }
}
