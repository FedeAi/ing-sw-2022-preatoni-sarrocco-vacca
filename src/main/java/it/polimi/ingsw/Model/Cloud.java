package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.EnumMap;
import java.util.Map;

public class Cloud {
    private Map<Color, Integer> students;

    public Map<Color, Integer> pickStudents() {
        Map<Color, Integer> returnStudents = students;
        students = new EnumMap<Color, Integer>(Color.class);
        return returnStudents;
    }

    public boolean isEmpty(){
        return students.size()==0;
    }

    public void addStudent(Color color) {
        students.put(color, students.getOrDefault(color, 0) + 1);
    }

    public void addStudents(Map<Color, Integer> addStudents) {
        if(students.size()>0){
            students.forEach(
                    (key, value) -> addStudents.merge(key, value, Integer::sum));
        }
    }

}
