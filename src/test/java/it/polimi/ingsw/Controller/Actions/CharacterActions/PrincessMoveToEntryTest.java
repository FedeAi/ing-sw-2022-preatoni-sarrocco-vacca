package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Princess;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrincessMoveToEntryTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    private Princess card;
    private List<CharacterCard> cardList;
    private Color selectionColor;
    private int selectionValue;

    @BeforeEach
    private void init() {
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
        card = new Princess("", game.getBag());
        card.init();
        cardList = new ArrayList<>();
        cardList.add(card);
        game.initCharacterCards(cardList);
        // We need to have at least 1 of a color on the card to activate it, so we cycle through the colors
        selectionColor = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = card.getStudentsMap().getOrDefault(c, 0);
            if (selectionValue > 0) {
                selectionColor = c;
                break;
            }
        }
        action = new PrincessMoveToEntry(p1.getNickname(), selectionColor);
        card.activate(gameManager.getRules(), game);
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

    @DisplayName("No princesses in the game test")
    @Test
    void noPrincesses() {
        game.initCharacterCards(new ArrayList<>());
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active princesses test")
    @Test
    void notActive() {
        card.deactivate(gameManager.getRules(), game);
        game.setGameState(GameState.PRINCESS_MOVE_STUDENT);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Max color in the hall test")
    @Test
    void maxHall() {
        for (int i = 0; i < Constants.SCHOOL_LANE_SIZE; i++) {
            p1.getSchool().addStudentHall(selectionColor);
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Princess move to entry test")
    @Test
    void princessCard() {

        int prevHall = p1.getSchool().getStudentsHall().getOrDefault(selectionColor, 0);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(prevHall + 1, p1.getSchool().getStudentsHall().getOrDefault(selectionColor, 0));
    }
}
