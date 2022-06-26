package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.TowerColor;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {
    private Random RANDOM = new Random();

    @Test
    void getStudentsEntry() {
        Bag bag = new Bag(50);
        Map<Color, Integer> students = bag.extract(RANDOM.nextInt(50));
        School school = new School(8, TowerColor.BLACK, students);
        for (Map.Entry<Color, Integer> student : students.entrySet()) {
            assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
        }
    }

    @Test
    void addStudentEntry() {
        Bag bag = new Bag(50);
        Map<Color, Integer> students = bag.extract(RANDOM.nextInt(50));
        School school = new School(8, TowerColor.BLACK, new EnumMap<Color, Integer>(students));

        Color newStudent = Color.randomColor();
        school.addStudentEntry(newStudent);
        for (Map.Entry<Color, Integer> student : students.entrySet()) {
            if (!student.getKey().equals(newStudent)) {
                assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
            } else {
                assertEquals(student.getValue() + 1, school.getStudentsEntry().get(student.getKey()));
            }
        }
    }

    @Test
    void addStudentsEntry() {
        Bag bag = new Bag(50);
        Map<Color, Integer> initialStudents = bag.extract(RANDOM.nextInt(5));
        Map<Color, Integer> addStudents = bag.extract(RANDOM.nextInt(5));
        School school = new School(8, TowerColor.BLACK, initialStudents);
        for (Map.Entry<Color, Integer> student : initialStudents.entrySet()) {
            assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
        }

        //addStudentsEntry
        school.addStudentsEntry(addStudents);
        for (Map.Entry<Color, Integer> student : school.getStudentsEntry().entrySet()) {
            int expected = initialStudents.getOrDefault(student.getKey(), 0) + addStudents.getOrDefault(student.getKey(), 0);
            assertEquals(expected, student.getValue());
        }
    }

    @Test
    void getStudentsHall() {
        Map<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        Color newStudent = Color.randomColor();
        students.put(newStudent, 5);
        School school = new School(8, TowerColor.BLACK, new EnumMap<Color, Integer>(students));

        assertNotNull(school.getStudentsHall());
        assertEquals(0, school.getStudentsHall().size());

        school.addStudentHall(newStudent);
        school.addStudentHall(newStudent);
        assertEquals(2, school.getStudentsHall().get(newStudent));
    }

    @Test
    void addStudentHall() {
        Map<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        Color newStudent = Color.randomColor();
        students.put(newStudent, 5);
        School school = new School(8, TowerColor.BLACK, new EnumMap<Color, Integer>(students));

        assertNotNull(school.getStudentsHall());
        assertEquals(0, school.getStudentsHall().size());

        school.addStudentHall(newStudent);
        school.addStudentHall(newStudent);
        assertEquals(2, school.getStudentsHall().get(newStudent));

        // check that the entry has not been modified
        for (Map.Entry<Color, Integer> student : students.entrySet()) {
            assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
        }
    }


    @Test
    void moveStudentFromEntryToHall() {
        Map<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        Color newStudent = Color.randomColor();
        students.put(newStudent, 5);
        School school = new School(8, TowerColor.BLACK, new EnumMap<Color, Integer>(students));

        school.moveStudentFromEntryToHall(newStudent);
        school.moveStudentFromEntryToHall(newStudent);

        assertEquals(3, school.getStudentsEntry().get(newStudent));
        assertEquals(2, school.getStudentsHall().get(newStudent));

    }

    @Test
    void removeStudentFromEntry() {

        Map<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        Color newStudent = Color.randomColor();
        students.put(newStudent, 5);
        School school = new School(8, TowerColor.BLACK, new EnumMap<Color, Integer>(students));

        school.moveStudentFromEntryToHall(newStudent);
        school.moveStudentFromEntryToHall(newStudent);

        assertEquals(3, school.getStudentsEntry().get(newStudent));
        assertEquals(2, school.getStudentsHall().get(newStudent));

        // remove student from entry
        school.removeStudentFromEntry(newStudent);
        assertEquals(2, school.getStudentsEntry().get(newStudent));
        assertEquals(2, school.getStudentsHall().get(newStudent));
    }


}