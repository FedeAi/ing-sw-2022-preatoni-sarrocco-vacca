package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Cloud {
    private Map<Color, Integer> students;

    public Cloud() {
        students = new EnumMap<Color, Integer>(Color.class);
    }

    public Map<Color, Integer> pickStudents() {
        Map<Color, Integer> returnStudents = students;
        students = new EnumMap<Color, Integer>(Color.class);
        return returnStudents;
    }

    public boolean isEmpty() {
        return students.size() == 0;
    }

    public void addStudent(Color color) {
        students.put(color, students.getOrDefault(color, 0) + 1);
    }

    public void addStudents(Map<Color, Integer> addStudents) {
        for (Map.Entry<Color, Integer> entry : addStudents.entrySet()) {
            students.put(entry.getKey(), entry.getValue() + students.getOrDefault(entry.getKey(), 0));
        }
    }

}
