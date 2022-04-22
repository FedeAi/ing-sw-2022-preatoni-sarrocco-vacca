package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.Map;

public abstract class Island {
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
}
