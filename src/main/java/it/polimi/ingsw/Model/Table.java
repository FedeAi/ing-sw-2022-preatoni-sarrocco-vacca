package it.polimi.ingsw.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Table {
    private ArrayList<Island> islands;
    private MotherNature motherNature;
    private ArrayList<CharacterCard> characterCards; //array, fixed size = 3
    private Bag bag;
    private Turn turn;


    // The table constructor creates the bag with the specified students inside. It then initializes the islands.
    // Proper testing to be added
    public Table(int motherNatureIsland) {
        bag = new Bag(2);
        for(int i = 0; i < 12; i++) {
            // Properly treat the MNI's opposite (different init)
            if(i == motherNatureIsland) {
                islands.add(new Island());
                motherNature = new MotherNature(islands.get(i));
            }
            else {
                ArrayList<Student> students = bag.extract(2);
                islands.add(new Island());
                islands.get(i).addStudents(students);
            }
        }
        // Recreate the bag with the correct amount of students after the initial setup
        bag = new Bag(24);
    }

    // Rather than returning the reference to the bag, we expose the extraction of the student itself
    public Bag getBag() {
        return bag;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }
}
