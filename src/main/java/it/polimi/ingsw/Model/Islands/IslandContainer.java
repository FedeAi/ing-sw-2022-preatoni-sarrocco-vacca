package it.polimi.ingsw.Model.Islands;

import java.util.LinkedList;
import java.util.List;

public class IslandContainer {
    private LinkedList<Island> islands;
    public IslandContainer(LinkedList<Island> islands){
        this.islands = islands;
    }
    public IslandContainer(){
        this.islands = new LinkedList<>();
    }
    public int prevIslandIndex(int currIslandIndex) {
        return (currIslandIndex + islands.size() - 1) % islands.size();
    }
    public int nextIslandIndex(int currIslandIndex) {
        return (currIslandIndex + islands.size() + 1) % islands.size();
    }

    public Island prevIsland(int currIslandIndex) {
        return islands.get(prevIslandIndex(currIslandIndex));
    }
    public Island nextIsland(int currIslandIndex) {
        return islands.get(nextIslandIndex(currIslandIndex));
    }

    public void joinPrevIsland(int currIslandIndex){
        if(currIslandIndex>0 && currIslandIndex < islands.size()) {
            Island currIsland = islands.get(currIslandIndex);
            Island prevIsland = prevIsland(currIslandIndex);
            Island superIsland = new SuperIsland(List.of(prevIsland, currIsland));
            islands.add(prevIslandIndex(currIslandIndex), superIsland);
            islands.remove(currIsland);
            islands.remove(prevIsland);
        }
    }
    public void joinNextIsland(int currIslandIndex){
        if(currIslandIndex>0 && currIslandIndex < islands.size()) {
            Island currIsland = islands.get(currIslandIndex);
            Island nextIsland = nextIsland(currIslandIndex);
            Island superIsland = new SuperIsland(List.of(currIsland,nextIsland));
            islands.add(currIslandIndex, superIsland);
            islands.remove(currIsland);
            islands.remove(nextIsland);
        }
    }

    public int size(){
        return islands.size();
    }
    public Island get(int index){
        return islands.get(index);
    }
}
