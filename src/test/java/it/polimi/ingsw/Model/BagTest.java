package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BagTest {

    @Test
    public void createNewBag() {

        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);

        assertEquals("size check", numStudents, myBag.getStudents().size());

        for (final Color color : Color.values()) {
            assertEquals("color check", numStudents / Color.values().length, myBag.getStudents().stream().filter(student -> student.equals(color)).count());
        }
    }

    @Test
    public void extract() {

        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        Map<Color, Integer> out = myBag.extract(8);
        assertEquals("control out dim ", out.values().stream().reduce(0, Integer::sum).intValue(), 8);
        assertEquals("control bag dim ", myBag.getStudents().size(), initialSize - 8);

    }

    @Test
    //corner case of extract(), so doesn't change the logic test
    public void extractOne() {

        int numStudents = 1 * Color.values().length; //point out that num students, 1 for each color
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        Color out = myBag.extractOne();
        assertEquals("control bag dim ", myBag.getStudents().size(), initialSize - 1);

    }
    @Test
    public void extendBag() {

        int extensionSize = Color.values().length;
        int numStudents = 1 * Color.values().length;
        Bag myBag = new Bag(numStudents);
        Bag myBag2 = new Bag(numStudents);
        myBag2.extendBag(extensionSize);

        assertEquals("the diff of the bags should be the extensionSize ", myBag.getStudents().size()+extensionSize, myBag2.getStudents().size());


    }

}