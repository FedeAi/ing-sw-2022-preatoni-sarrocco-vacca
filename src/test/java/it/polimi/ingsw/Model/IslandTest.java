package it.polimi.ingsw.Model;

import java.util.EnumMap;

import it.polimi.ingsw.Model.Enumerations.Color;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

class IslandTest {

    @org.junit.jupiter.api.Test
    @Test
    public void addStudentsTest() {
        int numStudents = 1;
        Island islandTest = new BaseIsland();
        Bag myBag = new Bag(numStudents);
        // First, we test the addition of a single student
        int initialSize = islandTest.getStudents().size();
        islandTest.addStudent(myBag.extractOne());
        int finalSize = islandTest.getStudents().size();
        Assertions.assertEquals(initialSize + numStudents, finalSize, "Check if final size is equals to initial size + add size");
        /*
                TODO FIX MULTIPLE ADD TEST
        // extraction of numStudents students to be added to the island, in order to test the method
        EnumMap<Color, Integer> students = new EnumMap<Color,Integer>(myBag.extract(numStudents)); // FIXME

        //islandTest.addStudents(students);     // FIXME

        Assertions.assertEquals(initialSize + numStudents, finalSize, "Check if final size is equals to initial size + add size");
        */
    }
}