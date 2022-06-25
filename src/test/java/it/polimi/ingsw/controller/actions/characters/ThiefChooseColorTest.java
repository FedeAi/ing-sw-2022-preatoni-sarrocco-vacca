package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Thief;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThiefChooseColorTest {
    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    private Thief card;
    private List<CharacterCard> cardList;
    private Color color;

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
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new Thief("");
        cardList = new ArrayList<>();
        cardList.add(card);
        game.initCharacterCards(cardList);
        color = Color.randomColor();
        action = new ThiefChooseColor(p1.getNickname(), color);
        card.activate(gameManager.getRules(), game);

        p1.getSchool().addStudentHall(color);
        p1.getSchool().addStudentHall(color);
        p1.getSchool().addStudentHall(color);
        p1.getSchool().addStudentHall(color);

        p2.getSchool().addStudentHall(color);
        p2.getSchool().addStudentHall(color);
    }

    @Test
    @DisplayName("Wrong state test")
    void wrongState() {
        // Now we try to perform the action with the wrong gameState set (set before)
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No thieves in the game test")
    @Test
    void noThieves() {
        game.initCharacterCards(new ArrayList<>());
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active princesses test")
    @Test
    void notActive() {
        card.deactivate(gameManager.getRules(), game);
        game.setGameState(GameState.THIEF_CHOOSE_COLOR);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong color test")
    @Test
    void noColor() {
        card.deactivate(gameManager.getRules(), game);
        action = new ThiefChooseColor(p1.getNickname(), null);
        card.activate(gameManager.getRules(), game);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Thief remove from hall test")
    @Test
    void thiefCard() {
        int hallSize1 = p1.getSchool().getStudentsHall().get(color);
        int hallSize2 = p2.getSchool().getStudentsHall().get(color);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        int finalHallSize1 = p1.getSchool().getStudentsHall().get(color);
        int finalHallSize2 = p2.getSchool().getStudentsHall().get(color);
        assertEquals(hallSize1 - 3, finalHallSize1);
        assertEquals(hallSize2 - 2, finalHallSize2);
    }
}