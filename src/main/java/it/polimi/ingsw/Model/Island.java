package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.Map;

public interface Island {
    public Map<Color, Integer> getStudents();

    public abstract String getOwner();

    public abstract int getNumTower();

    public abstract void setOwner(String owner);

    public abstract void addStudent(Color student);
}
