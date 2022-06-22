package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * IslandContainer class is the container of the game board, represented by the starting 12 islands.
 */
public class IslandContainer implements Serializable {

    private final LinkedList<Island> islands;

    /**
     * Constructor IslandContainer creates an instance with an empty island list.
     */
    public IslandContainer() {
        this.islands = new LinkedList<>();
    }

    /**
     * Constructor IslandContainer creates an instance with the specified islands.
     *
     * @param islands the list of the islands to be set to the container.
     */
    public IslandContainer(LinkedList<Island> islands) {
        this.islands = islands;
    }

    /**
     * Method prevIslandIndex returns the previous island index relative to the current one.
     *
     * @param currIslandIndex the current island index.
     */
    public int prevIslandIndex(int currIslandIndex) {
        return (currIslandIndex + islands.size() - 1) % islands.size();
    }

    /**
     * Method prevIslandIndex returns the next island index relative to the current one.
     *
     * @param currIslandIndex the current island index.
     */
    public int nextIslandIndex(int currIslandIndex) {
        return (currIslandIndex + islands.size() + 1) % islands.size();
    }

    /**
     * Method correctIndex returns the absolute movement relative to the current position
     * (E.G. this addresses a movement bigger than the size of the islands).
     *
     * @param delta           the movement positions.
     * @param currentPosition the current motherNature position.
     */
    public int correctIndex(int delta, int currentPosition) {
        return (currentPosition + islands.size() + delta) % islands.size();
    }

    /**
     * Method addIslandStudent adds a student to the specified island.
     *
     * @param islandIndex the index to put the student on.
     * @param student     the color of the selected student.
     */
    public void addIslandStudent(int islandIndex, Color student) {
        islands.get(islandIndex).addStudent(student);
    }

    /**
     * Method prevIsland returns the previous island reference relative to the current island index.
     *
     * @param currIslandIndex the current island index.
     */
    public Island prevIsland(int currIslandIndex) {
        return islands.get(prevIslandIndex(currIslandIndex));
    }

    /**
     * Method nextIsland returns the next island reference relative to the current island index.
     *
     * @param currIslandIndex the current island index.
     */
    public Island nextIsland(int currIslandIndex) {
        return islands.get(nextIslandIndex(currIslandIndex));
    }

    /**
     * Method joinPrevIsland joins the island determined by the current island index with the previous one.
     *
     * @param currIslandIndex the current island index.
     * @see SuperIsland
     */
    public void joinPrevIsland(int currIslandIndex) {
        if (currIslandIndex >= 0 && currIslandIndex < islands.size()) {
            Island currIsland = islands.get(currIslandIndex);
            Island prevIsland = prevIsland(currIslandIndex);
            Island superIsland = new SuperIsland(List.of(prevIsland, currIsland));
            islands.add(prevIslandIndex(currIslandIndex), superIsland);
            islands.remove(currIsland);
            islands.remove(prevIsland);
        }
    }

    /**
     * Method joinNextIsland joins the island determined by the current island index with the next one.
     *
     * @param currIslandIndex the current island index.
     * @see SuperIsland
     */
    public void joinNextIsland(int currIslandIndex) {
        if (currIslandIndex >= 0 && currIslandIndex < islands.size()) {
            Island currIsland = islands.get(currIslandIndex);
            Island nextIsland = nextIsland(currIslandIndex);
            Island superIsland = new SuperIsland(List.of(currIsland, nextIsland));
            islands.add(currIslandIndex, superIsland);
            islands.remove(currIsland);
            islands.remove(nextIsland);
        }
    }

    /**
     * Method isFeasibleIndex checks if the provided index is valid.
     *
     * @param index the island index.
     * @return true if the index is present in the island list, false otherwise.
     */
    public boolean isFeasibleIndex(int index) {
        return index >= 0 && index < islands.size();
    }

    /**
     * Method size returns the number of islands in the IslandContainer.
     */
    public int size() {
        return islands.size();
    }

    /**
     * Method setOwner sets an island ownership to a certain player.
     *
     * @param island the island index on which the ownership is going to be changed.
     * @param owner  the nickname of the owner to be set.
     */
    public void setOwner(int island, String owner) {
        islands.get(island).setOwner(owner);
    }

    /**
     * Method setIslandBlocked set the blocked (or unblocked) status (no influence calculation) on a selected island.
     * This implements the Grandma CharacterCard.
     *
     * @param island    the selected island index to be blocked.
     * @param isBlocked the blocking flag.
     * @see it.polimi.ingsw.Model.Cards.CharacterCards.Grandma
     * @see it.polimi.ingsw.Controller.Actions.CharacterActions.GrandmaBlockIsland
     */
    public void setIslandBlocked(int island, Boolean isBlocked) {
        islands.get(island).setBlocked(isBlocked);
    }

    /**
     * Method get returns the copy of the wanted BaseIsland or SuperIsland.
     *
     * @param index the index of the island to be copied.
     */
    public Island get(int index) {
        if (islands.get(index) instanceof BaseIsland) {
            return new BaseIsland((BaseIsland) islands.get(index));
        } else if (islands.get(index) instanceof SuperIsland) {
            return new SuperIsland((SuperIsland) islands.get(index));
        } else {
            throw new RuntimeException("The island is neither a BaseIsland nor a SuperIsland");
        }
    }

    /**
     * Method getIslands returns the copy of the islands list.
     */
    public List<Island> getIslands() {
        return new ArrayList<>(islands);
    }
}