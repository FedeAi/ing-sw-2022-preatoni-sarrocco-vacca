package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.InvalidIndexException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChooseCloudTest {

    GameManager gameManager;
    Player p1, p2;
    Game game;
    Performable action;
    Random r;
    int selection;

    @BeforeEach
    private void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Fede");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        gameManager.initGame();
        game.setRoundOwner(p2);
        game.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        r = new Random();
        selection = r.nextInt(game.numPlayers());
        action = new ChooseCloud(p2.getNickname(), selection);
    }

    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong cloud index test")
    @Test
    void wrongIndex() {
        selection = -1;
        action = new ChooseCloud(p2.getNickname(), selection);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        selection = game.getPlayers().size() + 1;
        action = new ChooseCloud(p2.getNickname(), selection);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Cloud empty test")
    @Test
    void emptyCloud() {
        game.pickCloud(selection);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Cloud selection test")
    @Test
    void chooseCloud() {
        int studentsPerTurn = Rules.getStudentsPerTurn(game.numPlayers());
        Map<Color, Integer> prevEntry = new EnumMap<>(p2.getSchool().getStudentsEntry());
        // Cloud cloud = game.getClouds().get(selection);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        Map<Color, Integer> postEntry = new EnumMap<>(p2.getSchool().getStudentsEntry());
        int previousPlayerStudents = prevEntry.values().stream().reduce(0, Integer::sum);
        int postPlayerStudents = postEntry.values().stream().reduce(0, Integer::sum);

        assertEquals(previousPlayerStudents + studentsPerTurn, postPlayerStudents, "The correct number of student has been added");
        //assertTrue(cloud.isEmpty(), "after having picked students the cloud must be empty"); // Not true if the current player is the last one (refill)
    }
}