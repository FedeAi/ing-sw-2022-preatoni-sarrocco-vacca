package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Model.Enumerations.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SuperIslandTest {
    private static final Random RANDOM = new Random();

    private BaseIsland randomInitBaseIsland() {
        BaseIsland baseIsland = new BaseIsland();
        for (int i = 0; i < RANDOM.nextInt(100); i++) {
            baseIsland.addStudent(Color.values()[RANDOM.nextInt(Color.values().length)]);
        }
        return baseIsland;
    }

    private SuperIsland randomInitSuperIsland() {
        List<Island> islandList = new ArrayList<>();
        for (int i = 0; i < RANDOM.nextInt(5); i++) {
            islandList.add(randomInitBaseIsland());
        }
        return new SuperIsland(islandList);
    }

    @Test
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

    @Test
    void getStudents() {
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

    @Test
    void getOwner() {
        BaseIsland baseIslandA = randomInitBaseIsland();
        baseIslandA.setOwner("ErColosseo");
        BaseIsland baseIslandB = randomInitBaseIsland();
        SuperIsland s1 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        assertEquals("ErColosseo", s1.getOwner());
    }


    @Test
    void getNumTower() {
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

    @Test
    void setOwner() {
        BaseIsland baseIslandA = randomInitBaseIsland();
        BaseIsland baseIslandB = randomInitBaseIsland();
        BaseIsland baseIslandC = randomInitBaseIsland();
        BaseIsland baseIslandD = randomInitBaseIsland();
        SuperIsland s1 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        SuperIsland s2 = new SuperIsland(List.of(s1, baseIslandC, baseIslandD));

        s2.setOwner("Giampiero");
        assertEquals("Giampiero", s2.getOwner());
        assertEquals("Giampiero", baseIslandB.getOwner());
    }

    @Test
    void addStudent() {
        BaseIsland baseIslandA = randomInitBaseIsland();
        BaseIsland baseIslandB = randomInitBaseIsland();
        BaseIsland baseIslandC = randomInitBaseIsland();
        BaseIsland baseIslandD = randomInitBaseIsland();
        SuperIsland s1 = new SuperIsland(List.of(baseIslandA, baseIslandB));
        SuperIsland s2 = new SuperIsland(List.of(s1, baseIslandC, baseIslandD));
        Color student = Color.YELLOW;
        int numYellowStudents = s2.getStudents().getOrDefault(student, 0);
        s2.addStudent(student);
        assertEquals(numYellowStudents + 1, s2.getStudents().getOrDefault(student, 0));
    }
}