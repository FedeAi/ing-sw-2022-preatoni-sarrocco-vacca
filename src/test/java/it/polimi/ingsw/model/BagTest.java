package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Method BagTest tests the Bag class.
 *
 * @see Bag
 */
public class BagTest {

    /**
     * Method bagCreation tests that the Bag is initialized correctly.
     *
     * @see Bag#Bag(int)
     */
    @Test
    @DisplayName("Bag creation test")
    public void bagCreation() {
        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);

        assertEquals(numStudents, myBag.getStudents().size(), "size check");

        for (final Color color : Color.values()) {
            assertEquals(numStudents / Color.values().length, myBag.getStudents().stream().filter(student -> student.equals(color)).count(), "color check");
        }
    }

    /**
     * Method extract tests that the Bag correctly extracts and removes students.
     *
     * @see Bag#extract(int)
     */
    @Test
    @DisplayName("Bag multiple extraction test")
    public void extract() {
        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        Map<Color, Integer> out = myBag.extract(8);
        assertEquals(out.values().stream().reduce(0, Integer::sum).intValue(), 8, "control out dim");
        assertEquals(myBag.getStudents().size(), initialSize - 8, "control bag dim");
    }

    /**
     * Method extract tests that the Bag correctly extracts and removes a single student.
     *
     * @see Bag#extract()
     */
    @Test
    @DisplayName("Bag single extraction test")
    public void extractOne() {
        // Point out that num students, 1 for each color
        int numStudents = Color.values().length;
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        myBag.extract();
        assertEquals(myBag.getStudents().size(), initialSize - 1, "control bag dim");
    }

    /**
     * Method extendBag checks if a Bag is correctly extended.
     */
    @Test
    @DisplayName("Bag extension test")
    public void extendBag() {
        int extensionSize = Color.values().length;
        int numStudents = Color.values().length;
        Bag myBag = new Bag(numStudents);
        Bag myBag2 = new Bag(numStudents);
        myBag2.extendBag(extensionSize);
        assertEquals(myBag.getStudents().size() + extensionSize, myBag2.getStudents().size(), "the diff of the bags should be the extensionSize ");
    }
}