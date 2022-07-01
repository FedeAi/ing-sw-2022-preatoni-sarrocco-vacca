package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.constants.Color;

import java.io.Serializable;
import java.util.Map;

/**
 * Island class is an abstract specification to be extended either with a BaseIsland or a SuperIsland.
 */
public abstract class Island implements Serializable {

    protected boolean isBlocked;
    protected String owner;

    /**
     * Constructor Island sets the initial blocked flag.
     */
    protected Island() {
        isBlocked = false;
    }

    /**
     * Method getStudents is the abstract of the getter method for the island's students.
     */
    public abstract Map<Color, Integer> getStudents();

    /**
     * Method getOwner returns the current owner of an island.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Method getNumTower is the abstract of the getter method for the island's number of towers (relevant to the superIsland).
     */
    public abstract int getNumTower();

    /**
     * Method setOwner sets the ownership of an island to the specified owner.
     *
     * @param owner the nickname of the owner to be set.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Method addStudent is the abstract of the method for putting students on an island.
     */
    public abstract void addStudent(Color student);

    /**
     * Method checkJoin checks if two islands have the same owner in order for the merge between them to happen.
     *
     * @param island1 the first island reference.
     * @param island2 the second island reference.
     * @return true if the islands are owner by the same player, false otherwise.
     */
    public static boolean checkJoin(Island island1, Island island2) {
        if (island1.getOwner() == null || island2.getOwner() == null) {
            return false;
        }
        return island1.getOwner().equals(island2.getOwner());
    }

    /**
     * Method isBlocked returns the status of the blocking on the island.
     * This is relative to the Grandma CharacterCard.
     *
     * @return true if the island is blocked, false otherwise.
     * @see it.polimi.ingsw.model.cards.characters.Grandma
     * @see it.polimi.ingsw.controller.actions.characters.GrandmaBlockIsland
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Method setBlocked set the blocked (or not) status on an island.
     * This is relative to the Grandma CharacterCard.
     *
     * @see it.polimi.ingsw.model.cards.characters.Grandma
     * @see it.polimi.ingsw.controller.actions.characters.GrandmaBlockIsland
     */
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    /**
     * Method size returns the number of islands of which an Island is composed of (1 by default).
     * This is relevant to the SuperIsland.
     *
     * @see SuperIsland
     */
    public int size() {
        return 1;
    }

}
