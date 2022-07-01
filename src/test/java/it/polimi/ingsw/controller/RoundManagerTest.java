package it.polimi.ingsw.controller;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.actions.*;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method RoundManagerTest tests the RoundManager class.
 *
 * @see RoundManager
 */
class RoundManagerTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    Player secondOwner, finalOwner;

    /**
     * Method init initializes the values needed for the test.
     */
    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Davide");
        game.createPlayer(2, "Fede");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        p3 = game.getPlayers().get(2);
        gameManager.initGame();
        game.setRoundOwner(p1);
    }

    /**
     * Method checkPlayerActionOrder checks to player order after different actions.
     */
    @Test
    @DisplayName("Check player action order")
    void checkPlayerActionOrder() {
        action = new ChooseMagician(p1.getNickname(), 0);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        action = new ChooseMagician(p2.getNickname(), 0);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        action = new ChooseMagician(p3.getNickname(), 0);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }


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

    /**
     * Method handleStateChange checks if the state is correctly updated after an action.
     */
    @Test
    @DisplayName("Basic state of game test")
    void handleStateChange() {
        action = new ChooseMagician(p1.getNickname(), 0);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        action = new ChooseMagician(p2.getNickname(), 0);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        action = new ChooseMagician(p3.getNickname(), 0);
        try {
            gameManager.performAction(action);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // PLANNING PHASE
        game.setGameState(GameState.PLANNING_CHOOSE_CARD);
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
        gameManager.handleNewRoundOwnerOnDisconnect(game.getRoundOwner().getNickname());
        game.getWaitingPlayersReconnected();
        game.reEnterWaitingPlayers();
        game.getPlayerByID(0);
        game.fireInitialState();

    }
}