package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Bag {
    private final ArrayList<Color> students;

    public Bag(int numStudentEachColor) {
        students = new ArrayList<>();
        for (int i = 0; i < numStudentEachColor * Color.values().length; i++) {
            students.add(Color.values()[i % Color.values().length]);
        }
    }

    public ArrayList<Color> getStudents() {
        return students;
    }

    public Map<Color, Integer> extract(int numStudent) {
        Map<Color, Integer> out = new EnumMap<Color, Integer>(Color.class);
        Random rand = new Random();
        for (int i = 0; i < numStudent; i++) {
            int randomIndex = rand.nextInt(students.size());
            Color randomElement = students.get(randomIndex);
            students.remove(randomIndex);
            out.put(randomElement, out.getOrDefault(randomElement, 0) + 1);
        }
        return out;
    }

    public Color extractOne() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(students.size());
        Color randomElement = students.get(randomIndex);
        students.remove(randomIndex);
        return randomElement;
    }
}

