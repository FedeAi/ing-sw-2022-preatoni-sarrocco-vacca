package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;

public class Table {
    private ArrayList<Island> islands;
    private MotherNature motherNature;
    private ArrayList<CharacterCard> characterCards; //array, fixed size = 3
    private Bag bag;
    private ActionPhase actionPhase;


    // The table constructor creates the bag with the specified students inside. It then initializes the islands.
    // Proper testing to be added
    public Table(int motherNatureIsland) {
        bag = new Bag(2);
        int opposite = (motherNatureIsland + 6) % 12;

        for(int i = 0; i < 12; i++) {
            islands.add(new Island());
            if(i == motherNatureIsland) {
                motherNature = new MotherNature(islands.get(i));
            }
            else if(i != opposite) {
                ArrayList<Student> students = bag.extract(2);
                islands.get(i).addStudents(students);
            }
        }

        // Setting all the next islands.
        for(int i = 0; i < 11; i++) {
            islands.get(i).setNextIsland(islands.get(i + 1));
        }
        islands.get(11).setNextIsland(islands.get(0));

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
