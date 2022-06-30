package it.polimi.ingsw.model.islands;

import it.polimi.ingsw.constants.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SuperIslandTest class tests the SuperIsland class.
 */
class SuperIslandTest {

    private static final Random RANDOM = new Random();

    /**
     * Method randomInitBaseIsland creates and returns a new BaseIsland instance with random students.
     *
     * @return The reference to the created BaseIsland.
     */
    private BaseIsland randomInitBaseIsland() {
        BaseIsland baseIsland = new BaseIsland();
        for (int i = 0; i < RANDOM.nextInt(100); i++) {
            baseIsland.addStudent(Color.values()[RANDOM.nextInt(Color.values().length)]);
        }
        return baseIsland;
    }

    /**
     * Method constructor tests the creation of new SuperIsland instances.
     */
    @Test
    @DisplayName("SuperIsland creation test")
    void constructor() {
        // Testing the different possibility to build a super island
        BaseIsland baseIslandA = randomInitBaseIsland();
        BaseIsland baseIslandB = randomInitBaseIsland();
        BaseIsland baseIslandC = randomInitBaseIsland();

        SuperIsland s1 = new SuperIsland(List.of(baseIslandA));
        assertEquals(1, s1.getBaseIslands().size());
        assertEquals(baseIslandA, s1.getBaseIslands().get(0));

        SuperIsland s2 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        assertEquals(2, s2.getBaseIslands().size());
        assertTrue(s2.getBaseIslands().containsAll(List.of(baseIslandA, baseIslandB)));

        SuperIsland s3 = new SuperIsland(List.of(s2, baseIslandC));
        assertEquals(3, s3.getBaseIslands().size());
        assertTrue(s3.getBaseIslands().containsAll(List.of(baseIslandA, baseIslandB, baseIslandC)));
    }

    /**
     * Method addStudents tests the addition of students to different SuperIsland instances.
     */
    @Test
    @DisplayName("SuperIsland student add test")
    void addStudents() {
        BaseIsland baseIslandA = randomInitBaseIsland();
        BaseIsland baseIslandB = randomInitBaseIsland();
        BaseIsland baseIslandC = randomInitBaseIsland();

        SuperIsland s1 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        int totalBluStudents = baseIslandA.getStudents().getOrDefault(Color.BLUE, 0) + baseIslandB.getStudents().getOrDefault(Color.BLUE, 0);
        int totalYellowStudents = baseIslandA.getStudents().getOrDefault(Color.YELLOW, 0) + baseIslandB.getStudents().getOrDefault(Color.YELLOW, 0);
        assertEquals(totalBluStudents, s1.getStudents().getOrDefault(Color.BLUE, 0));
        assertEquals(totalYellowStudents, s1.getStudents().getOrDefault(Color.YELLOW, 0));

        SuperIsland s2 = new SuperIsland(List.of(s1, baseIslandC));
        totalBluStudents += baseIslandC.getStudents().getOrDefault(Color.BLUE, 0);
        totalYellowStudents += baseIslandC.getStudents().getOrDefault(Color.YELLOW, 0);
        assertEquals(totalBluStudents, s2.getStudents().getOrDefault(Color.BLUE, 0));
        assertEquals(totalYellowStudents, s2.getStudents().getOrDefault(Color.YELLOW, 0));
    }

    /**
     * Method changeOwner tests the change of ownership of a SuperIsland.
     */
    @Test
    @DisplayName("SuperIsland test change of owner")
    void changeOwner() {
        BaseIsland baseIslandA = randomInitBaseIsland();
        baseIslandA.setOwner("ErColosseo");
        BaseIsland baseIslandB = randomInitBaseIsland();
        SuperIsland s1 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        assertEquals("ErColosseo", s1.getOwner());
        s1.setOwner("Franco");
        assertEquals("Franco", s1.getOwner());
    }

    /**
     * Method superIslandTowers tests the number of towers returned by a SuperIsland.
     */
    @Test
    @DisplayName("SuperIsland number of towers test")
    void superIslandTowers() {
        BaseIsland baseIslandA = randomInitBaseIsland();
        BaseIsland baseIslandB = randomInitBaseIsland();
        BaseIsland baseIslandC = randomInitBaseIsland();
        BaseIsland baseIslandD = randomInitBaseIsland();
        SuperIsland s1 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        SuperIsland s2 = new SuperIsland(List.of(s1, baseIslandC, baseIslandD));

        assertEquals(0, s1.getNumTower());
        assertEquals(0, s2.getNumTower());

        s1.setOwner("Giampiero");
        s2.setOwner("Giampiero");

        assertEquals(2, s1.getNumTower());
        assertEquals(4, s2.getNumTower());
    }
}