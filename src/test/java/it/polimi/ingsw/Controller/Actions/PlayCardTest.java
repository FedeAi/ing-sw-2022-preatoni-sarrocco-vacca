package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayCardTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    Random r;
    int selection;

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
        game.setGameState(GameState.PLANNING_CHOOSE_CARD);
        game.setRoundOwner(p3);
        r = new Random();
        selection = r.nextInt(1, 10 + 1);
        action = new PlayCard(p3.getNickname(), selection);
    }

    @DisplayName("Invalid player test")
    @Test
    void nullPlayer() {
        action = new PlayCard("", selection);
        assertThrows(InvalidPlayerException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Check if playcard is performable")
    @Test
    void canPlayCard() {
        assertDoesNotThrow(() -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Wrong state set test")
    void wrongStateTest() {
        // checks if the game is set to the correct game state
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Wrong roundOwner test")
    void wrongRoundOwnerTest() {
        // checking if the player argument is the actual round owner
        // p3 is the actual round owner, while I try to pass p1
        action = new PlayCard(p1.getNickname(), selection);
        assertThrows(RoundOwnerException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        action = new PlayCard(p3.getNickname(), selection);
        game.setRoundOwner(p3);
        assertDoesNotThrow(() -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong card selection test")
    @Test
    void wrongSelectionTest() {
        game.playCard(p3, p3.getCards().get(selection - 1));
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        action = new PlayCard(p3.getNickname(), 11);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Card already played test")
    @Test
    void cardAlreadyPlayed() {
        game.setPlayersActionPhase(List.of(p2, p1));
        game.setPlanningOrder();
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        game.setRoundOwner(p1);
        action = new PlayCard(p1.getNickname(), selection);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Basic playCard action test")
    @Test
    void playCard() {
        AssistantCard c = p3.getCards().get(selection - 1);
        List<AssistantCard> prevCards = new ArrayList<>(p3.getCards());
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertFalse(p3.hasCard(selection));
        assertEquals(p3.getPlayedCard(), c);
        assertEquals(prevCards.size(), p3.getCards().size() + 1);
        prevCards.remove(c);
        assertTrue(p3.getCards().containsAll(prevCards));
        game.setPlayersActionPhase(List.of(p3, p2, p1));
        game.setPlanningOrder();
        game.setRoundOwner(action.nextPlayer(game, gameManager.getRules()));
        assertEquals(1, game.getPlayedCards().size());
        assertTrue(game.getPlayedCards().contains(c));
        action.nextState(game, gameManager.getRules());
    }
}