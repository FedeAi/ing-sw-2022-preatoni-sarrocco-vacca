package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentFromEntryToHallTest {
    private GameManager gameManager;
    private Player p1;
    private Player p2;
    private Game gameInstance;

    private void initGame() {
        gameManager = new GameManager(new Game());
        p1 = new Player("ManovellismoOrdinario");
        p2 = new Player("Biella");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();

        gameInstance = gameManager.getGame();
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        gameInstance.setRoundOwner(p2);
    }

    @Test
    void canPerformExt() {
        initGame();

        Color student = getStudentFromEntry(p2); // get a student of the player Entry
        Performable moveStudentsToHallAction = new MoveStudentFromEntryToHall("Biella", student);
        Performable moveStudentsToHallActionWrongPlayer = new MoveStudentFromEntryToHall("Gianfranco", student);

        // base case
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertTrue(moveStudentsToHallAction.canPerform(gameInstance, gameManager.getRules()));

        //wrong game phase
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);
        assertFalse(moveStudentsToHallAction.canPerform(gameInstance, gameManager.getRules()));

        // wrong player ( no player with that nickname )
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertFalse(moveStudentsToHallActionWrongPlayer.canPerform(gameInstance, gameManager.getRules()));
        // wrong player is not your turn
        gameInstance.setRoundOwner(p1);
        assertFalse(moveStudentsToHallAction.canPerform(gameInstance, gameManager.getRules()));
        gameInstance.setRoundOwner(p2);


        // The player has already moved all possible students
        Performable action = new MoveStudentFromEntryToHall("Biella", student);
        for (int i = 0; i < Rules.getStudentsPerTurn(gameInstance.numPlayers()); i++) {
            Color pickedStudent = getStudentFromEntry(p2);
            action = new MoveStudentFromEntryToHall("Biella", pickedStudent);
            assertTrue(action.canPerform(gameInstance, gameManager.getRules()));

            p2.getSchool().moveStudentFromEntryToHall(pickedStudent);
        }
        assertFalse(action.canPerform(gameInstance, gameManager.getRules()));
    }

    @Test
    void performMove_HallEntry() {
        initGame();

        Color student = getStudentFromEntry(p2); // get a student of the player Entry
        Performable moveStudentsToHallAction = new MoveStudentFromEntryToHall("Biella", student);
        assertTrue(moveStudentsToHallAction.canPerform(gameInstance, gameManager.getRules()));


        // previous state
        Map<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry());
        Map<Color, Integer> prevHall = new EnumMap<Color, Integer>(p2.getSchool().getStudentsHall());
        // perform move
        moveStudentsToHallAction.performMove(gameInstance, gameManager.getRules());

        // checks
        // entry has not been modified except...
        Player p = gameInstance.getPlayerByNickname("Biella").get();
        for (Map.Entry<Color, Integer> entry : prevEntry.entrySet()) {
            int students = p.getSchool().getStudentsEntry().get(entry.getKey());
            if (entry.getKey() != student) {
                assertEquals(entry.getValue(), students);
            } else {
                assertEquals(entry.getValue(), students + 1);
            }
        }
        // hall has not been modified except...
        for (Map.Entry<Color, Integer> entry : prevHall.entrySet()) {
            int students = p.getSchool().getStudentsHall().get(entry.getKey());
            if (entry.getKey() != student) {
                assertEquals(entry.getValue(), students);
            } else {
                assertEquals(entry.getValue(), students - 1, "hall has not been modified except...");
            }
        }
    }

    @Test
    void performMove_professorCheck() {
        initGame();
        Color student = getStudentFromEntry(p2); // get a student of the player Entry
        Performable moveStudentsToHallAction = new MoveStudentFromEntryToHall(p2.getNickname(), student);
        assertTrue(moveStudentsToHallAction.canPerform(gameInstance, gameManager.getRules()));

        moveStudentsToHallAction.performMove(gameInstance, gameManager.getRules());

        // check that professor is gained after a move
        assertEquals(gameInstance.getProfessors().get(student), p2.getNickname(), "check that professor is gained after a move");
    }

    private Color getStudentFromEntry(Player p) {
        return p.getSchool().getStudentsEntry().entrySet().stream().filter((s) -> s.getValue() > 0).findFirst().get().getKey();
    }
}