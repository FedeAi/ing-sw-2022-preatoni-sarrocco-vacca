package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Constants.Color;

import java.io.Serializable;
import java.util.Map;

public abstract class Island implements Serializable {
    /*
        TODO I HAVEN'T SORTED THE SUPERISLAND BLOCKED CASE YET
        If we join an island with an adjacent one which is blocked,
        is the SuperIsland blocked or not?
    */

    protected boolean isBlocked;
    protected String owner;

    protected Island(){
        isBlocked = false;
    }

    public abstract Map<Color, Integer> getStudents();

    public String getOwner() {
        return owner;
    }

    public abstract int getNumTower();

    public void setOwner(String owner) {
        this.owner = owner;
    }

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

    public int size() {
        return 1;
    }
}
