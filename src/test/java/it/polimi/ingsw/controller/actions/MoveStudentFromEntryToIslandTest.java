package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentFromEntryToIslandTest {

    GameManager gameManager;
    Game game;
    Player p1;
    Player p2;
    Performable action;
    Random r;
    Color student;
    int index;

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
        r = new Random();
        student = getStudentFromEntry(p2);
        index = r.nextInt(0, 12);
        action = new MoveStudentFromEntryToIsland(p2.getNickname(), student, index);
    }

    @DisplayName("Wrong island index test")
    @Test
    void wrongIndex() {
        index = -1;
        action = new MoveStudentFromEntryToIsland(p2.getNickname(), student, index);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        index = game.getIslandContainer().size();
        action = new MoveStudentFromEntryToIsland(p2.getNickname(), student, index);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Move student to island test")
    @Test
    void moveToIsland() {
        Map<Color, Integer> prevEntry = new EnumMap<>(p2.getSchool().getStudentsEntry());
        Map<Color, Integer> prevIsland = new EnumMap<>(game.getIslandContainer().get(index).getStudents());
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // Checks if the number of students in the entry has correctly been modified
        int colorValue;
        for (Map.Entry<Color, Integer> entry : prevEntry.entrySet()) {
            colorValue = p2.getSchool().getStudentsEntry().get(entry.getKey());
            if (entry.getKey() != student) {
                assertEquals(entry.getValue(), colorValue);
            } else {
                assertEquals(entry.getValue() - 1, colorValue);
            }
        }
        // Checks if the number of students on the island has correctly been modified
        for (Map.Entry<Color, Integer> islandStudents : game.getIslandContainer().get(index).getStudents().entrySet()) {
            if (islandStudents.getKey() != student) {
                assertEquals(prevIsland.getOrDefault(islandStudents.getKey(), 0), islandStudents.getValue());
            } else {
                assertEquals(prevIsland.getOrDefault(islandStudents.getKey(), 0) + 1, islandStudents.getValue());
            }
        }
    }

    private Color getStudentFromEntry(Player p) {
        return p.getSchool().getStudentsEntry().entrySet().stream().filter((s) -> s.getValue() > 0).findFirst().get().getKey();
    }
}