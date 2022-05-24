package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivateCardTest {
    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    int selection;

    /**
     * Method init: initializes values.
     */
    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Davide");
        p3 = new Player(2, "Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        game = gameManager.getGame();
        game.setExpertMode(true);
        gameManager.initGame();
        game.setRoundOwner(p3);
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        selection = 1;
        action = new ActivateCard(p3.getNickname(), selection);
    }

    @DisplayName("Wrong nickname test")
    @Test
    void wrongNickname() {
        String wrongNickname = "Wrong";
        action = new ActivateCard(wrongNickname, selection);
        assertThrows(InvalidPlayerException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong roundOwner test")
    @Test
    void wrongRoundOwner() {
        action = new ActivateCard(p1.getNickname(), selection);
        assertThrows(RoundOwnerException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.PLANNING_CHOOSE_CARD);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong game mode test")
    @Test
    void normalMode() {
        game.setExpertMode(false);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong card selection test")
    @Test
    void invalidSelection() {
        selection = -1;
        action = new ActivateCard(p3.getNickname(), selection);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        selection = 10;
        action = new ActivateCard(p3.getNickname(), selection);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Not enough money test")
    @Test
    void noMoney() {
        p3.spendCoins(p3.getBalance());
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Card already active test")
    @Test
    void alreadyActive() {
        CharacterCard card = game.getCharacterCards().get(selection);
        for (int i = 0; i < card.getPrice() + 1; i++) {
            game.incrementPlayerBalance(p3.getNickname());
        }
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        for (int i = 0; i < card.getPrice() + 1; i++) {
            game.incrementPlayerBalance(p3.getNickname());
        }
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Max one card per turn test")
    @Test
    void cardPerTurn() {
        CharacterCard card = game.getCharacterCards().get(selection);
        for (int i = 0; i < card.getPrice() + 1; i++) {
            game.incrementPlayerBalance(p3.getNickname());
        }
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        for (int i = 0; i < card.getPrice() + 1; i++) {
            game.incrementPlayerBalance(p3.getNickname());
        }
        card.deactivate(gameManager.getRules(), game);
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        /*
        FIXME
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        */
    }

    @DisplayName("Card activation test")
    @Test
    void activateCard() {
        CharacterCard card = game.getCharacterCards().get(selection);
        for (int i = 0; i < card.getPrice() + 1; i++) {
            game.incrementPlayerBalance(p3.getNickname());
        }
        int previousPlayerBalance = p3.getBalance();
        int previousGameBalance = game.getBalance();
        int previousCardPrice = card.getPrice();
        // This is due to cards costing more than the initial balance
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(previousPlayerBalance - previousCardPrice, p3.getBalance(), "money are removed correctly");
        assertEquals(previousGameBalance + previousCardPrice - 1, game.getBalance(), "first activation of the card, game get back price - 1");
        assertEquals(previousCardPrice + 1, card.getPrice(), "cost increment on card is right");
        card.deactivate(gameManager.getRules(), game);
        // Give some money to P3, in order to try to activate a card again
        for (int i = 0; i < card.getPrice() + 1; i++) {
            game.incrementPlayerBalance(p3.getNickname());
        }
        /* check that in the second activation price is manged correctly */
        previousPlayerBalance = p3.getBalance();
        previousGameBalance = game.getBalance();
        previousCardPrice = card.getPrice();
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(previousPlayerBalance - previousCardPrice, p3.getBalance(), "money are removed correctly");
        assertEquals(previousGameBalance + previousCardPrice, game.getBalance(), "second activation of the card, game get back full price");
        assertEquals(previousCardPrice, card.getPrice(), "no cost increment on card is right");
    }
}