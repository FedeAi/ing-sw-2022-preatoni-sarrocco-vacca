package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Knight;
import it.polimi.ingsw.model.cards.characters.Monk;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MonkMoveToIslandTest {

    Performable action;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;
    Monk card;
    List<CharacterCard> cardList;
    Random r;
    int index;
    Color selection;
    int selectionValue;

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
        card = new Monk("", game.getBag());
        card.init();
        cardList = new ArrayList<>();
        r = new Random();
        index = r.nextInt(0, 12);
        // We need to have at least 1 of a color on the card to activate it, so we cycle through the colors
        selection = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = card.getStudentsMap().getOrDefault(c, 0);
            if (selectionValue > 0) {
                selection = c;
                break;
            }
        }
        action = new MonkMoveToIsland(p1.getNickname(), selection, index);
    }

    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
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
        action = new MonkMoveToIsland(p1.getNickname(), selection, index);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        index = game.getIslandContainer().size();
        action = new MonkMoveToIsland(p1.getNickname(), selection, index);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No monks in the current game test")
    @Test
    void noMonks() {
        // Now we're going to have some cards in the list, but not of the MONK type
        Knight tempCard = new Knight("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        game.setGameState(GameState.MONK_MOVE_STUDENT);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active characters test")
    @Test
    void noActiveCards() {
        game.setGameState(GameState.MONK_MOVE_STUDENT);
        cardList.add(card);
        game.initCharacterCards(cardList);
        // Now we try to have no active card in the list while having a correct islandIndex
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Monk doesn't have the specified student test")
    @Test
    void noStudent() {
        cardList.add(card);
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        Color missing = Color.BLUE;
        for (Color c : Color.values()) {
            if (card.getStudentsMap().getOrDefault(c, 0) == 0) {
                missing = c;
                break;
            }
        }
        action = new MonkMoveToIsland(p1.getNickname(), missing, index);
        // Now we try to have no active card in the list while having a correct islandIndex
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Monk move student to island test")
    @Test
    void monkCard() {
        cardList.add(card);
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        Island island = game.getIslandContainer().get(index);
        int islValue = island.getStudents().getOrDefault(selection, 0);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(islValue + 1, island.getStudents().get(selection));
        assertFalse(card.isActive());
    }
}