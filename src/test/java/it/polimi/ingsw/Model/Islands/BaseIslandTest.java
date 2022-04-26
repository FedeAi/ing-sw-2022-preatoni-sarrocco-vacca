package it.polimi.ingsw.Model.Islands;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BaseIslandTest {

    //    private GameManager initGameManager(){
//        GameManager gameManager = new GameManager();
//        Player p1 = new Player("Marula");
//        Player p2 = new Player("Albano");
//        gameManager.addPlayer(p1);
//        gameManager.addPlayer(p2);
//        gameManager.initGame();
//        return gameManager;
//    }
    @Test
    void getStudents() {
        List<Color> students = new ArrayList<>();
        students.add(Color.BLUE);
        students.add(Color.BLUE);
        students.add(Color.BLUE);
        students.add(Color.YELLOW);
        students.add(Color.YELLOW);

        BaseIsland baseIsland = new BaseIsland();
        for (Color student : students) {
            baseIsland.addStudent(student);
        }
        assertEquals(3, baseIsland.getStudents().get(Color.BLUE));
        assertEquals(2, baseIsland.getStudents().get(Color.YELLOW));
        assertEquals(0, baseIsland.getStudents().getOrDefault(Color.GREEN, 0));
        assertEquals(0, baseIsland.getStudents().getOrDefault(Color.PINK, 0));

    }

    @Test
    void getOwner() {
        BaseIsland baseIsland = new BaseIsland();
        baseIsland.setOwner("Albano");
        assertEquals("Albano", baseIsland.getOwner());
    }

    @Test
    void getNumTower() {
        BaseIsland baseIsland = new BaseIsland();
        assertEquals(0, baseIsland.getNumTower());
        baseIsland.setOwner("Albano");
        assertEquals(1, baseIsland.getNumTower());
    }

    @Test
    void setOwner() {
        BaseIsland baseIsland = new BaseIsland();
        baseIsland.setOwner("Albano");
        assertEquals("Albano", baseIsland.getOwner());
    }

    @Test
    void addStudent() {
        List<Color> blueStudents = new ArrayList<>();
        List<Color> yellowStudents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            blueStudents.add(Color.BLUE);
        }
        for (int i = 0; i < 5; i++) {
            yellowStudents.add(Color.YELLOW);
        }

        BaseIsland baseIsland = new BaseIsland();
        for (int i = 0; i < blueStudents.size(); i++) {
            Color student = blueStudents.get(i);
            baseIsland.addStudent(student);
            assertEquals(i + 1, baseIsland.getStudents().get(Color.BLUE));
            assertEquals(0, baseIsland.getStudents().getOrDefault(Color.YELLOW, 0));
        }
        for (int i = 0; i < yellowStudents.size(); i++) {
            Color student = yellowStudents.get(i);
            baseIsland.addStudent(student);
            assertEquals(10, baseIsland.getStudents().get(Color.BLUE));
            assertEquals(i + 1, baseIsland.getStudents().get(Color.YELLOW));
        }
    }
}