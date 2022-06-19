package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.MonkCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
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
    MonkCharacter card;
    List<CharacterCard> cardList;
    Random r;
    int index;
    Color selection;
    int selectionValue;

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
        card = new MonkCharacter("", game.getBag());
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
        KnightCharacter tempCard = new KnightCharacter("");
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