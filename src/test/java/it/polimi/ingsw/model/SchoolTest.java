package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.TowerColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SchoolTest class tests the School class.
 *
 * @see School
 */
class SchoolTest {

    private Random RANDOM = new Random();

    /**
     * Method getStudentsEntry tests if a School is correctly set to the initial students extracted from the bag.
     *
     * @see School#School(int, TowerColor, Map)
     */
    @Test
    @DisplayName("School entry creation test")
    void getStudentsEntry() {
        Bag bag = new Bag(50);
        Map<Color, Integer> students = bag.extract(RANDOM.nextInt(50));
        School school = new School(8, TowerColor.BLACK, students);
        for (Map.Entry<Color, Integer> student : students.entrySet()) {
            assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
        }
    }

    /**
     * Method addStudentEntry tests if a single student is correctly added to the School's entry.
     *
     * @see School#addStudentEntry(Color)
     */
    @Test
    @DisplayName("Add a single student to the entry test")
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

    /**
     * Method addStudentsEntry tests if multiple students are correctly added to the School's entry.
     *
     * @see School#addStudentsEntry(Map)
     */
    @Test
    @DisplayName("Add multiple students to the entry test")
    void addStudentsEntry() {
        Bag bag = new Bag(50);
        Map<Color, Integer> initialStudents = bag.extract(RANDOM.nextInt(5));
        Map<Color, Integer> addStudents = bag.extract(RANDOM.nextInt(5));
        School school = new School(8, TowerColor.BLACK, initialStudents);
        for (Map.Entry<Color, Integer> student : initialStudents.entrySet()) {
            assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
        }
        school.addStudentsEntry(addStudents);
        for (Map.Entry<Color, Integer> student : school.getStudentsEntry().entrySet()) {
            int expected = initialStudents.getOrDefault(student.getKey(), 0) + addStudents.getOrDefault(student.getKey(), 0);
            assertEquals(expected, student.getValue());
        }
    }

    /**
     * Method getStudentsHall tests if a School's hall is correctly initialized,
     * then it checks if the student addition to the hall works as expected.
     *
     * @see School#addStudentHall(Color)
     */
    @Test
    @DisplayName("Hall creation test")
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

    /**
     * Method getStudentsHall tests if a School's hall is correctly initialized,
     * then it checks if the student addition to the hall works as expected.
     *
     * @see School#addStudentHall(Color)
     */
    @Test
    @DisplayName("Hall addition test")
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

        // Checks if the entry has not been modified
        for (Map.Entry<Color, Integer> student : students.entrySet()) {
            assertEquals(student.getValue(), school.getStudentsEntry().get(student.getKey()));
        }
    }

    /**
     * Method moveStudentFromEntryToHall tests if a student is correctly moved from the School's entry to the hall.
     *
     * @see School#moveStudentFromEntryToHall(Color)
     */
    @Test
    @DisplayName("Move student from entry to hall test")
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

    /**
     * Method removeStudentFromEntry tests if a student is correctly removed from the School's entry.
     *
     * @see School#removeStudentFromEntry(Color)
     */
    @Test
    @DisplayName("Remove a student from the entry test")
    void removeStudentFromEntry() {
        Map<Color, Integer> students = new EnumMap<Color, Integer>(Color.class);
        Color newStudent = Color.randomColor();
        students.put(newStudent, 5);
        School school = new School(8, TowerColor.BLACK, new EnumMap<Color, Integer>(students));

        school.moveStudentFromEntryToHall(newStudent);
        school.moveStudentFromEntryToHall(newStudent);

        assertEquals(3, school.getStudentsEntry().get(newStudent));
        assertEquals(2, school.getStudentsHall().get(newStudent));

        // Remove a student from the entry
        school.removeStudentFromEntry(newStudent);
        assertEquals(2, school.getStudentsEntry().get(newStudent));
        assertEquals(2, school.getStudentsHall().get(newStudent));
    }
}