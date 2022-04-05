package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BagTest {

    @Test
    public void createNewBag(){

        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);

        assertEquals("size check",numStudents, myBag.getStudents().size());

        for(final Color color : Color.values()){
            assertEquals("color check", numStudents / Color.values().length, myBag.getStudents().stream().filter( student -> student.equals(color)).count());
        }
    }

    @Test
    public void extract() {
        int numStudents = 5 * Color.values().length;
        Bag myBag = new Bag(numStudents);
        int initialSize = myBag.getStudents().size();
        Map<Color, Integer> out = myBag.extract(8);
        assertEquals("control out dim ",out.values().stream().reduce(0, Integer::sum).intValue(), 8 );
        assertEquals("control bag dim ",myBag.getStudents().size(), initialSize - 8);

    }
//    public void testGetStudents() {
//    }
}