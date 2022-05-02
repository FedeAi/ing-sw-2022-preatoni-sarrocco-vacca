package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Constants.Color;

import java.util.Map;

public abstract class Island {
    /*
        TODO I HAVEN'T SORTED THE SUPERISLAND BLOCKED CASE YET
        If we join an island with an adjacent one which is blocked,
        is the SuperIsland blocked or not?
    */
    protected boolean isBlocked;

    public abstract Map<Color, Integer> getStudents();

    public abstract String getOwner();

    public abstract int getNumTower();

    public abstract void setOwner(String owner);

    public abstract void addStudent(Color student);

    public static boolean checkJoin(Island island1, Island island2) {
        if (island1.getOwner() == null || island2.getOwner() == null) {
            return false;
        }
        return island1.getOwner().equals(island2.getOwner());
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
