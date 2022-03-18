package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Cloud {
    private ArrayList<Student> students;

    public Cloud(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
