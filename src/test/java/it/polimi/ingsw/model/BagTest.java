package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Color;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BagTest {

    @Test
    public void bagCreation() {
        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);

        assertEquals(numStudents, myBag.getStudents().size(), "size check");

        for (final Color color : Color.values()) {
            assertEquals(numStudents / Color.values().length, myBag.getStudents().stream().filter(student -> student.equals(color)).count(), "color check");
        }
    }

    @Test
    public void extract() {
        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        Map<Color, Integer> out = myBag.extract(8);
        assertEquals(out.values().stream().reduce(0, Integer::sum).intValue(), 8, "control out dim");
        assertEquals(myBag.getStudents().size(), initialSize - 8, "control bag dim");
    }

    @Test
    //corner case of extract(), so doesn't change the logic test
    public void extractOne() {
        // Point out that num students, 1 for each color
        int numStudents = Color.values().length;
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        Color out = myBag.extract();
        assertEquals(myBag.getStudents().size(), initialSize - 1, "control bag dim");
    }

    @Test
    public void extendBag() {
        int extensionSize = Color.values().length;
        int numStudents = Color.values().length;
        Bag myBag = new Bag(numStudents);
        Bag myBag2 = new Bag(numStudents);
        myBag2.extendBag(extensionSize);
        assertEquals(myBag.getStudents().size() + extensionSize, myBag2.getStudents().size(), "the diff of the bags should be the extensionSize ");
    }
}