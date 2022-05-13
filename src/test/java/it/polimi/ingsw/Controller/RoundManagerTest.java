package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundManagerTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    Player secondOwner, finalOwner;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game());
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Davide");
        p3 = new Player(2, "Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setRoundOwner(p1);
    }

    @Test
    @DisplayName("Check player action order")
    void checkPlayerActionOrder() {
        action = new PlayCard(p1.getNickname(), 1);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        secondOwner = game.getRoundOwner();
        assertNotEquals(secondOwner, p1);
        assertNotEquals(secondOwner, p3);

        action = new PlayCard(secondOwner.getNickname(), 3);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        finalOwner = game.getRoundOwner();
        assertNotEquals(finalOwner, p1);
        assertNotEquals(finalOwner, p2);

        action = new PlayCard(finalOwner.getNickname(), 2);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(game.getRoundOwner().getNickname(), p1.getNickname());

        String p = game.getNextPlayerActionPhase().get();
        assertEquals(p, p3.getNickname());
        game.setRoundOwner(p3);

        p = game.getNextPlayerActionPhase().get();
        assertEquals(p, p2.getNickname());
        game.setRoundOwner(p2);

    }

    @Test
    @DisplayName(" test")
    void performeAction() {
    }

    @Test
    @DisplayName("Basic state of game test")
    void handleStateChange() {
        //2 cases: make an action that doesn't change the state and another one that it chan
        // doesn't change the state of game
        // PLANNING PHASE
        GameState prevState = game.getGameState();
        action = new PlayCard(game.getRoundOwner().getNickname(), 1);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        GameState nextState = game.getGameState();
        assertEquals(prevState, nextState, "only first player played card -> no state change");


        prevState = game.getGameState();
        action = new PlayCard(game.getRoundOwner().getNickname(), 3);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        nextState = game.getGameState();
        assertEquals(prevState, nextState, "also the second player did the playcard move");


        prevState = game.getGameState();
        action = new PlayCard(game.getRoundOwner().getNickname(), 2);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        nextState = game.getGameState();
        assertNotEquals(prevState, nextState, "with the last player we have a change of state");
        assertEquals(nextState, GameState.ACTION_MOVE_STUDENTS);

        prevState = game.getGameState();
        // ACTION PHASE - move students
        for (int i = 0; i < Rules.getStudentsPerTurn(game.numPlayers()); i++) {
            Player p = game.getRoundOwner();
            Color studentToMove = p.getSchool().getStudentsEntry().entrySet().stream().filter(colorIntegerEntry -> colorIntegerEntry.getValue() > 0).findFirst().get().getKey();
            action = new MoveStudentFromEntryToHall(p.getNickname(), studentToMove);
            try {
                gameManager.performAction(action);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }

        nextState = game.getGameState();
        assertNotEquals(prevState, nextState, "after ended the action phase move students, we must move mother nature");
        assertEquals(nextState, GameState.ACTION_MOVE_MOTHER);

        // ACTION PHASE - move mother
        prevState = game.getGameState();
        Player p = game.getRoundOwner();
        int move = p.getPlayedCard().getMaxMoves(); //check
        action = new MoveMotherNature(p.getNickname(), move);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        nextState = game.getGameState();
        assertNotEquals(prevState, nextState, "after move mother, we must choose a cloud");
        assertEquals(nextState, GameState.ACTION_CHOOSE_CLOUD);

        // ACTION PHASE - choose cloud
        prevState = game.getGameState();
        p = game.getRoundOwner();
        int cloud = 0;
        action = new ChooseCloud(p.getNickname(), cloud);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        nextState = game.getGameState();
        assertNotEquals(prevState, nextState, "after choose cloud, it's next player turn");
        assertEquals(nextState, GameState.ACTION_MOVE_STUDENTS);

        assertNotEquals(p.getNickname(), game.getRoundOwner().getNickname(), "after choose cloud, it's next player turn");
        assertEquals(game.getRoundOwner().getNickname(), p3.getNickname());

    }

}