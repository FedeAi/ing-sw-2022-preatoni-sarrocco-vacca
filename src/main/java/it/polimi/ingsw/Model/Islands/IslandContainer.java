package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class IslandContainer implements Serializable {
    private final LinkedList<Island> islands;

    public IslandContainer(LinkedList<Island> islands) {
        this.islands = islands;
    }

    public IslandContainer() {
        this.islands = new LinkedList<>();
    }

    public int prevIslandIndex(int currIslandIndex) {
        return (currIslandIndex + islands.size() - 1) % islands.size();
    }

    public int nextIslandIndex(int currIslandIndex) {
        return (currIslandIndex + islands.size() + 1) % islands.size();
    }

    public int correctIndex(int delta, int currentPosition) {
        return (currentPosition + islands.size() + delta) % islands.size();
    }

    public void addIslandStudent(int islandIndex, Color student) {
        islands.get(islandIndex).addStudent(student);
    }

    public Island prevIsland(int currIslandIndex) {
        return islands.get(prevIslandIndex(currIslandIndex));
    }

    public Island nextIsland(int currIslandIndex) {
        return islands.get(nextIslandIndex(currIslandIndex));
    }

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

    public void joinNextIsland(int currIslandIndex) {
        if (currIslandIndex >= 0 && currIslandIndex < islands.size() - 1) {
            Island currIsland = islands.get(currIslandIndex);
            Island nextIsland = nextIsland(currIslandIndex);
            Island superIsland = new SuperIsland(List.of(currIsland, nextIsland));
            islands.add(currIslandIndex, superIsland);
            islands.remove(currIsland);
            islands.remove(nextIsland);
        } else if (currIslandIndex == islands.size() - 1) {
            Island currIsland = islands.get(currIslandIndex);
            Island nextIsland = nextIsland(currIslandIndex);
            Island superIsland = new SuperIsland(List.of(currIsland, nextIsland));
            islands.add(0, superIsland);
            islands.remove(currIsland);
            islands.remove(nextIsland);
        }
    }

    public boolean isFeasibleIndex(int index) {
        return index >= 0 && index < islands.size();
    }

    public int size() {
        return islands.size();
    }

    public void setOwner(int island, String owner){
        islands.get(island).setOwner(owner);
    }

    public void setIslandBlocked(int island, Boolean isBlocked){
        islands.get(island).setBlocked(isBlocked);
    }


    public Island get(int index) {
        if (islands.get(index) instanceof BaseIsland){
            return new BaseIsland((BaseIsland)islands.get(index));
        } else if (islands.get(index) instanceof SuperIsland) {
            return new SuperIsland((SuperIsland) islands.get(index));
        }
        else{
            throw new RuntimeException("The island is neither a BaseIsland nor a SuperIsland");
        }
    }
}
