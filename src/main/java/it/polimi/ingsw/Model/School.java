package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.TowerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class School {
    private List<TowerColor> towers;
    private Map<Color, Integer> studentsHall;
    private Map<Color, Integer> studentsEntry;
    // TODO MOVE PROFESSORS TO GAME CLASS
    // private ArrayList<> professors;
    private final int laneSize = 9;

    // implement 4 player version (?), exceptions
    public School(int players, int playerNum) {
       towers = new ArrayList<TowerColor>();
        TowerColor color = null;

        if (playerNum == 0) {
            color = TowerColor.BLACK;
        } else if (playerNum == 1) {
            color = TowerColor.WHITE;
        } else if (playerNum == 2) {
            color = TowerColor.GRAY;
        }

        if (players == 2) {
            for (int i = 0; i < 8; i++) {
                towers.add(color);
            }
        } else if (players == 3) {
            for (int i = 0; i < 6; i++) {
                towers.add(color);
            }
        }
        //else
        //throw new InvalidPlayersException;
    }

    public List<TowerColor> getTowers() {
        return towers;
    }

    public Map<Color, Integer> getStudentsEntry() {
        return studentsEntry;
    }

    public void addStudentEntry(Color color) {
        studentsEntry.put(color, studentsEntry.getOrDefault(color, 0) + 1);
    }

    public Map<Color, Integer> getStudentsHall() {
        return studentsHall;
    }

    public void addStudentHall(Color color) {
        studentsHall.put(color, studentsEntry.getOrDefault(color, 0) + 1);
    }


}
