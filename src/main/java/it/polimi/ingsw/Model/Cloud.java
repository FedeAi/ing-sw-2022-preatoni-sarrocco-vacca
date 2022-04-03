package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.Map;

public class Cloud {
    private Map<Color, Integer> students;

    public Map<Color, Integer> getStudents() {
        return students;
    }

    public void addStudents(Color color) {
        students.put(color, 1);
    }
}
