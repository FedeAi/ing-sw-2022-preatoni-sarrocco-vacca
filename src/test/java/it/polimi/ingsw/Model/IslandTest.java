package it.polimi.ingsw.Model;

import java.util.ArrayList;

import it.polimi.ingsw.Model.Enumerations.Color;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

class IslandTest {

    @org.junit.jupiter.api.Test
    @Test
    public void addStudentsTest() {
        int numStudents = 5;
        Island islandTest = new Island();
        Bag myBag = new Bag(numStudents);
        // extraction of numStudents students to be added to the island, in order to test the method
        ArrayList<Color> students = new ArrayList<>(myBag.extract(numStudents)); // FIXME
        int initialSize = islandTest.getStudents().size();
        //islandTest.addStudents(students);     // FIXME
        int finalSize = islandTest.getStudents().size();
        Assertions.assertEquals(initialSize + numStudents, finalSize, "Check if final size is equals to initial size + add size");
    }
}