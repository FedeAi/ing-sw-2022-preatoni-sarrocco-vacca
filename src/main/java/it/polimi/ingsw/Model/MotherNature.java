package it.polimi.ingsw.Model;

public class MotherNature {

    private int currentIsland;

    public MotherNature(int currentIsland) {
        this.currentIsland = currentIsland;
    }

    public int getPosition() {
        return currentIsland;
    }

    public void setIsland(int index) {
        this.currentIsland = index;
    }
}
