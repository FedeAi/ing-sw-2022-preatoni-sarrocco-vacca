package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BagTest {

    @Test
    public void createNewBag(){

        int numStudentEachColor = 5;
        Bag myBag = new Bag(numStudentEachColor);

        assertEquals("size check", Color.values().length * numStudentEachColor, myBag.getStudents().size());
        //assertTrue(false);
        for(final Color color : Color.values()){
            assertEquals("color check", numStudentEachColor, myBag.getStudents().stream().filter( student -> student.getColor().equals(color)).count());
        }
    }

    @Test
    public void extract() {
        int numStudentEachColor = 5;
        Bag myBag = new Bag( numStudentEachColor);
        int initialSize = myBag.getStudents().size();
        ArrayList<Student> out = myBag.extract(8);
        assertEquals("control out dim ",out.size(), 8 );
        assertEquals("control bag dim ",myBag.getStudents().size(), initialSize - 8);

    }
//    public void testGetStudents() {
//    }
}