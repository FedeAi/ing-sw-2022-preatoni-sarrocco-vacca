package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Princess;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
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

/**
 * Class PrincessMoveToHallTest tests the PrincessMoveToHall action.
 *
 * @see PrincessMoveToHall
 */
public class PrincessMoveToHallTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    private Princess card;
    private List<CharacterCard> cardList;
    private Color selectionColor;
    private int selectionValue;

    /**
     * Method init initializes values.
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
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new Princess(game.getBag());
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
        action = new PrincessMoveToHall(p1.getNickname(), selectionColor);
        card.activate(gameManager.getRules(), game);
    }

    /**
     * Method wrongState tests if an action is created with the wrong state set.
     * PrincessMoveToHall action can only be performed in the Princess state.
     */
    @Test
    @DisplayName("Wrong state test")
    void wrongState() {
        // Now we try to perform the action with the wrong gameState set (set before)
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method noPrincesses checks if there are no Princesses in the current game.
     */
    @DisplayName("No princesses in the game test")
    @Test
    void noPrincesses() {
        game.initCharacterCards(new ArrayList<>());
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method noActives checks if there are no active character cards.
     */
    @DisplayName("No active princesses test")
    @Test
    void noActives() {
        card.deactivate(gameManager.getRules(), game);
        game.setGameState(GameState.PRINCESS_MOVE_STUDENT);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method maxHall checks if the player tries to move a student to a full hall.
     */
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

    /**
     * Method princessCard tests a valid PrincessMoveToHall action,
     * checking if the selected student is moved to the hall after the action.
     */
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
