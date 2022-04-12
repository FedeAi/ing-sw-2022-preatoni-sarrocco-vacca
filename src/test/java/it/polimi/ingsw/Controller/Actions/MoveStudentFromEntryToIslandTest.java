package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentFromEntryToIslandTest {

    @Test
    void performMove() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Player p2 = new Player("Davide");
        Player p3 = new Player("Fede");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p1);
        Performable moveStudent;
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        Color student = getStudentFromEntry(p1);
        int islandIndex = 2;
        moveStudent = new MoveStudentFromEntryToIsland(p1.getNickname(), student, islandIndex);
        assertTrue(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));

        Map<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p1.getSchool().getStudentsEntry());
        Map<Color, Integer> prevIsland = new EnumMap<Color, Integer>(p1.getSchool().getStudentsEntry());

        moveStudent.performMove(gameInstance, gameManager.getRules());

        // Checks if the number of students in the entry has correctly been modified
        int colorValue;
        for (Map.Entry<Color, Integer> entry : prevEntry.entrySet()) {
            colorValue = p1.getSchool().getStudentsEntry().get(entry.getKey());
            if (entry.getKey() != student) {
                assertEquals(entry.getValue(), colorValue);
            } else {
                assertEquals(entry.getValue(), colorValue + 1);
            }
        }

        // Checks if the number of students on the island has correctly been modified
        for (Map.Entry<Color, Integer> islandStudents : prevIsland.entrySet()) {
            colorValue = p1.getSchool().getStudentsEntry().get(islandStudents.getKey());
            if (islandStudents.getKey() != student) {
                assertEquals(islandStudents.getValue(), colorValue);
            } else {
                assertEquals(islandStudents.getValue(), colorValue + 1);
            }
        }
    }

    @Test
    void canPerformExt() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Player p2 = new Player("Davide");
        Player p3 = new Player("Fede");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p1);
        Performable moveStudent;
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        // Get a student from the player's entry
        Color student = getStudentFromEntry(p1);
        int islandIndex = 2;

        // An illegal player tries to play
        String missingPlayer = "Marco";
        moveStudent = new MoveStudentFromEntryToIsland(missingPlayer, student, islandIndex);
        assertFalse(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));

        // It's not the player's turn, roundOwner = p1, we're going to pass p2
        moveStudent = new MoveStudentFromEntryToIsland(p2.getNickname(), student, islandIndex);
        assertFalse(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));

        // We want to see if the action is performable, but we are in the wrong state
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);
        moveStudent = new MoveStudentFromEntryToIsland(p1.getNickname(), student, islandIndex);
        assertFalse(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        // Simple check for the islandIndex
        islandIndex = -1;
        moveStudent = new MoveStudentFromEntryToIsland(p1.getNickname(), student, islandIndex);
        assertFalse(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));
        islandIndex = 40;
        moveStudent = new MoveStudentFromEntryToIsland(p1.getNickname(), student, islandIndex);
        assertFalse(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));
        islandIndex = 2;

        // Base case
        moveStudent = new MoveStudentFromEntryToHall(p1.getNickname(), student);
        assertTrue(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));
        // We move all the allowed students
        for (int i = 0; i < Rules.getStudentsPerTurn(gameInstance.numPlayers()); i++) {
            Color pickedStudent = getStudentFromEntry(p1);
            moveStudent = new MoveStudentFromEntryToIsland(p1.getNickname(), pickedStudent, islandIndex);
            assertTrue(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));

            p1.getSchool().moveStudentFromEntryToHall(pickedStudent);
        }
        assertFalse(moveStudent.canPerformExt(gameInstance, gameManager.getRules()));
    }

    @Test
    public void getNickNamePlayerTest() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Player p2 = new Player("Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();

        Color color = getStudentFromEntry(p1);
        int islandIndex = 1;

        Performable moveStudent = new MoveStudentFromEntryToIsland(p1.getNickname(), color, islandIndex);
        assertEquals(p1.getNickname(), moveStudent.getNickNamePlayer());
    }

    // TODO MOVE THIS TO THE ABSTRACT (?)
    private Color getStudentFromEntry(Player p) {
        return p.getSchool().getStudentsEntry().entrySet().stream().filter((s) -> s.getValue() > 0).findFirst().get().getKey();
    }
}