package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.constants.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseIslandTest {

    /**
     * Method newOwnerTest tests if a new island owner is correctly set and the number of towers is updated.
     */
    @Test
    @DisplayName("Island conquer test")
    void newOwnerTest() {
        BaseIsland baseIsland = new BaseIsland();
        assertEquals(0, baseIsland.getNumTower());
        baseIsland.setOwner("Albano");
        assertEquals("Albano", baseIsland.getOwner());
        assertEquals(1, baseIsland.getNumTower());
    }

    /**
     * Method addStudentTest tests the correct student addition to an island.
     */
    @Test
    @DisplayName("Add student to an island test")
    void addStudentTest() {
        List<Color> blueStudents = new ArrayList<>();
        List<Color> yellowStudents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            blueStudents.add(Color.BLUE);
        }
        for (int i = 0; i < 5; i++) {
            yellowStudents.add(Color.YELLOW);
        }
        BaseIsland baseIsland = new BaseIsland();
        for (int i = 0; i < blueStudents.size(); i++) {
            Color student = blueStudents.get(i);
            baseIsland.addStudent(student);
            assertEquals(i + 1, baseIsland.getStudents().get(Color.BLUE));
            assertEquals(0, baseIsland.getStudents().getOrDefault(Color.YELLOW, 0));
        }
        for (int i = 0; i < yellowStudents.size(); i++) {
            Color student = yellowStudents.get(i);
            baseIsland.addStudent(student);
            assertEquals(10, baseIsland.getStudents().get(Color.BLUE));
            assertEquals(i + 1, baseIsland.getStudents().get(Color.YELLOW));
        }
    }
}