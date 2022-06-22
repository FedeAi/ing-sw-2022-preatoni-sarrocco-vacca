package it.polimi.ingsw.Model;

/**
 * MotherNature class represents the game Mother Nature, determined by its position.
 */
public class MotherNature {

    private int currentIsland;

    /**
     * Constructor MotherNature creates the MotherNature instance ands sets its current position.
     * @param currentIsland the island index to be set to MotherNature.
     */
    public MotherNature(int currentIsland) {
        this.currentIsland = currentIsland;
    }

    /**
     * Method getPosition returns MotherNature's current position.
     * @return The island index where MN currently is.
     */
    public int getPosition() {
        return currentIsland;
    }

    /**
     * Method setIsland sets MotherNature's position to the specified index.
     * @param index the island index to set the MN's position.
     */
    public void setIsland(int index) {
        this.currentIsland = index;
    }
}
