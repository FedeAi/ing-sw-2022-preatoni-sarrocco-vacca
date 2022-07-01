package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Bag class represents the game's random student extraction.
 */
public class Bag {
    private final ArrayList<Color> students;

    /**
     * Constructor Bag fills the student list.
     *
     * @param size the number of the students to be added.
     */
    public Bag(int size) {
        students = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            students.add(Color.values()[i % Color.values().length]);
        }
    }

    /**
     * Method extendBag adds students to the bag.
     * @param extensionSize the number of students to be added.
     */
    public void extendBag(int extensionSize) {
        for (int i = 0; i < extensionSize; i++) {
            students.add(Color.values()[i % Color.values().length]);
        }
    }

    /**
     * Method getStudents returns the reference to the students in the bag.
     * @return The list of the students in the bag.
     */
    public ArrayList<Color> getStudents() {
        return students;
    }

    /**
     *  Method extract returns and removes random students from the bag.
     * @param numStudent the number of students to extract.
     * @return The extracted student.
     */
    public Map<Color, Integer> extract(int numStudent) {
        Map<Color, Integer> out = new EnumMap<Color, Integer>(Color.class);
        Random rand = new Random();
        for (int i = 0; i < numStudent; i++) {
            if(students.size() > 0) {
                int randomIndex = rand.nextInt(students.size());
                Color randomElement = students.get(randomIndex);
                students.remove(randomIndex);
                out.put(randomElement, out.getOrDefault(randomElement, 0) + 1);
            }
        }
        return out;
    }

    /**
     *  Method extract returns and removes a random student from the bag.
     * @return The extracted student.
     */
    public Color extract() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(students.size());
        Color randomElement = students.get(randomIndex);
        students.remove(randomIndex);
        return randomElement;
    }
}