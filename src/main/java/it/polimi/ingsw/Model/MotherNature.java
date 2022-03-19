package it.polimi.ingsw.Model;

public class MotherNature {

    private Island currentIsland;

    public MotherNature(Island currentIsland) {
        this.currentIsland = currentIsland;
    }

    public Island getCurrent() {
        return currentIsland;
    }

    public void setIsland(Island nextIsland) {
        this.currentIsland = nextIsland;
    }
}
