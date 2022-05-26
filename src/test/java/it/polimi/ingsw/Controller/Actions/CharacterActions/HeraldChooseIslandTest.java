package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.MoveStudentFromEntryToHall;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.HeraldCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class HeraldChooseIslandTest {

    Performable action;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;
    int index;
    CharacterCard card;
    LinkedList<CharacterCard> cardList;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Fede");
        p3 = new Player(2, "Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setRoundOwner(p1);
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        index = 1;
        card = new HeraldCharacter("");
        cardList = new LinkedList<>();
        action = new HeraldChooseIsland(p1.getNickname(), index);
    }

    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active cards test")
    @Test
    void noActives() {
        card.activate(gameManager.getRules(), game);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

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

    /*
    @DisplayName("Herald conquer island test")
    @Test
    void heraldCard() {

        Color profColor = Color.BLUE;
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
        // FIXME THIS DOESNt WORK!
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
    */
}