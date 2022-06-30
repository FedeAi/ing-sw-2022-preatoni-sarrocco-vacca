package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.controller.actions.MoveStudentFromEntryToHall;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Herald;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class HeraldChooseIslandTest tests the HeraldChooseIsland action.
 *
 * @author Alessandro Vacca
 * @see HeraldChooseIsland
 */
class HeraldChooseIslandTest {

    Performable action;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;
    int index;
    CharacterCard card;
    LinkedList<CharacterCard> cardList;

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
        index = 1;
        card = new Herald("");
        cardList = new LinkedList<>();
        action = new HeraldChooseIsland(p1.getNickname(), index);
    }

    /**
     * Method wrongState tests if an action is created with the wrong state set.
     * HeraldChooseIsland action can only be performed in the Herald state.
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
     * Method noActives tests if there are no active cards in the current Game.
     */
    @DisplayName("No active cards test")
    @Test
    void noActives() {
        card.activate(gameManager.getRules(), game);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method wrongIndex tests if an action is created with an invalid index.
     */
    @DisplayName("Wrong island index test")
    @Test
    void wrongIndex() {
        cardList.add(card);
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        index = -1;
        action = new HeraldChooseIsland(p1.getNickname(), index);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        index = game.getIslandContainer().size();
        action = new HeraldChooseIsland(p1.getNickname(), index);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method heraldCard tests a valid HeraldChooseIsland action,
     * checking if the island is conquered after the action.
     */
    @DisplayName("Herald conquer island test")
    @Test
    void heraldCard() {
        Color profColor = Color.BLUE;
        p1.getSchool().addStudentEntry(profColor);
        action = new MoveStudentFromEntryToHall(p1.getNickname(), profColor);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        cardList.add(card);
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        // Now Ale controls the BLUE professor
        // Next we will be adding some students to the island we're going to trigger the card on
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(index).addStudent(profColor);
        }
        // Card trigger
        action = new HeraldChooseIsland(p1.getNickname(), index);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(p1.getNickname(), game.getIslandContainer().get(index).getOwner());
        index++;
        // We now try to conquer another island in order to join it with the previous one
        action = new HeraldChooseIsland(p1.getNickname(), index);
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(index).addStudent(profColor);
        }
        // Card trigger, and the current islandIndex will be joined with the previous one
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(p1.getNickname(), game.getIslandContainer().get(index - 1).getOwner());
        // Now we join the island with the next one
        index = 0;
        action = new HeraldChooseIsland(p1.getNickname(), index);
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(index).addStudent(profColor);
        }
        // Card trigger, and the current islandIndex will be joined with the previous one
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(p1.getNickname(), game.getIslandContainer().get(index).getOwner());
    }
}