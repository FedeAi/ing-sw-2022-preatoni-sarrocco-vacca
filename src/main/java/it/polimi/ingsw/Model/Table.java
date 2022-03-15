package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Table {
    private ArrayList<Island> islands;
    private MotherNature motherNature;
    private ArrayList<CharacterCard> characterCards; //array, fixed size = 3
    private Bag bag;
    private Turn turn;

    // Rather than returning the reference to the bag, we expose the extraction of the student itself dello student direttamente
    public Bag getBag() {
        return bag;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }
}
