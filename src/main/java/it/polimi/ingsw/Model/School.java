package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.TowerColor;

import java.util.ArrayList;

public class School {
    private ArrayList<Tower> towers;
    private ArrayList<Student> studentsHall;
    private ArrayList<Student> studentsEntry;
    private ArrayList<Professor> professors;
    private final int laneSize = 9;

    // implement 4 player version (?), exceptions
    public School(int players, int playerNum) {
        towers = new ArrayList<Tower>();
        TowerColor color = null;

        if(playerNum == 0) {
            color = TowerColor.BLACK;
        }

        else if(playerNum == 1) {
            color = TowerColor.WHITE;
        }

        else if(playerNum == 2) {
            color = TowerColor.GRAY;
        }

        if(players == 2) {
            for(int i = 0; i < 8; i++) {
                towers.add(new Tower(color));
            }
        }

        else if(players == 3) {
            for(int i = 0; i < 6; i++) {
                towers.add(new Tower(color));
            }
        }
        //else
            //throw new InvalidPlayersException;
        studentsEntry = new ArrayList<>();
        studentsHall = new ArrayList<>();
        professors = new ArrayList<>();
    }

    public int getNumTowers(){
        return towers.size();
    }

    public int getNumStudentsHall(Color color) {
        assert false;
        return 0;
    }

    public ArrayList<Student> getStudentsEntry() {
        return studentsEntry;
    }

    public boolean hasProfessor(Color color){
        assert false;
        return false;
    }
}
