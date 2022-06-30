package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MoveStudentFromEntryToHallTest class tests the MoveStudentFromEntryToHall action.
 *
 * @see MoveStudentFromEntryToHall
 */
class MoveStudentFromEntryToHallTest {

    GameManager gameManager;
    Game game;
    Player p1;
    Player p2;
    Performable action;
    Color student;

    /**
     * Method init initializes the values needed for the test.
     */
    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Fede");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        gameManager.initGame();
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        game.setRoundOwner(p2);
        student = getStudentFromEntry(p2);
        action = new MoveStudentFromEntryToHall(p2.getNickname(), student);
    }

    /**
     * Method wrongState tests if an action is created the wrong state set.
     * MoveStudentFromEntryToHall action can only be performed in the MoveStudents state.
     */
    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method maxStudentsMoved checks if a player has moved his allowed students per turn.
     */
    @DisplayName("Max students moved test")
    @Test
    void maxStudentsMoved() {
        for (int i = 0; i < Rules.getStudentsPerTurn(game.numPlayers()); i++) {
            p2.getSchool().moveStudentFromEntryToHall(student);
            student = getStudentFromEntry(p2);
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method moveToHall tests a basic MoveStudentFromEntryToHall action,
     * checking if a student is moved from the entry to the hall.
     */
    @DisplayName("Move student to hall test")
    @Test
    void moveToHall() {
        // Saving the previous maps, in order to assert the changes after the action
        Map<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry());
        Map<Color, Integer> prevHall = new EnumMap<Color, Integer>(p2.getSchool().getStudentsHall());
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // Checks that entry and hall have been modified only by the specified student
        for (Map.Entry<Color, Integer> entry : prevEntry.entrySet()) {
            int students = p2.getSchool().getStudentsEntry().get(entry.getKey());
            if (entry.getKey() != student) {
                assertEquals(entry.getValue(), students);
            } else {
                assertEquals(entry.getValue(), students + 1);
            }
        }
        for (Map.Entry<Color, Integer> entry : prevHall.entrySet()) {
            int students = p2.getSchool().getStudentsHall().get(entry.getKey());
            if (entry.getKey() != student) {
                assertEquals(entry.getValue(), students);
            } else {
                assertEquals(entry.getValue(), students - 1);
            }
        }
    }

    /**
     * Method professorConquer checks that a professor is gained after an action.
     */
    @DisplayName("Gain professor test")
    @Test
    void professorConquer() {
        // Checks that professor is gained after a move
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(game.getProfessors().get(student), p2.getNickname(), "check that professor is gained after having the max influence");
    }

    /**
     * Method fullHall checks that an action cannot be performed
     * if the player's hall is full (for the selected color).
     */
    @DisplayName("Max color in hall test")
    @Test
    void fullHall() {
        for (int i = 0; i < Constants.SCHOOL_LANE_SIZE; i++) {
            p2.getSchool().addStudentHall(student);
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method noStudents checks that an action cannot be performed
     * if the player's entry is empty (for the selected color).
     */
    @DisplayName("No students of the chosen color test")
    @Test
    void noStudents() {
        int n = p2.getSchool().getStudentsEntry().get(student);
        for (int i = 0; i < n; i++) {
            p2.getSchool().removeStudentFromEntry(student);
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method nextState tests that only after 3 moves (2 player mode) the state is changed.
     */
    @DisplayName("Next state test")
    @Test
    void nextState() {
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(action.nextState(game, gameManager.getRules()), game.getGameState());
        action = new MoveStudentFromEntryToHall(p2.getNickname(), getStudentFromEntry(p2));
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(action.nextState(game, gameManager.getRules()), game.getGameState());
        action = new MoveStudentFromEntryToHall(p2.getNickname(), getStudentFromEntry(p2));
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertNotEquals(action.nextState(game, gameManager.getRules()), game.getGameState());
        assertEquals(action.nextState(game, gameManager.getRules()), GameState.ACTION_MOVE_MOTHER);
    }

    /**
     * Method getStudentFromEntry returns the first student of the player's entry.
     *
     * @param p the Player reference.
     * @return The first student of the entry.
     */
    private Color getStudentFromEntry(Player p) {
        // Get the first student of the player Entry
        return p.getSchool().getStudentsEntry().entrySet().stream().filter((s) -> s.getValue() > 0).findFirst().get().getKey();
    }
}