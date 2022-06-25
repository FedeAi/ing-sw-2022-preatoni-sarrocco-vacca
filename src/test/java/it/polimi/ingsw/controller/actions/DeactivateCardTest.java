package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
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

class DeactivateCardTest {

    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    Performable action;
    LinkedList<CharacterCard> characterList;
    int selection;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Fede");
        game.createPlayer(2, "Davide");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        p3 = game.getPlayers().get(2);
        game.setExpertMode(true);
        gameManager.initGame();
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        game.setRoundOwner(p1);
        selection = 1;
        action = new DeactivateCard(p1.getNickname(), selection);
    }

    @DisplayName("No cards activated test")
    @Test
    void noActives() {
        // Then we try to perform the action with no characterCards present
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong card index test")
    @Test
    void wrongIndex() {
        selection = -1;
        action = new DeactivateCard(p1.getNickname(), selection);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        selection = game.getCharacterCards().size();
        action = new DeactivateCard(p1.getNickname(), selection);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Card wasn't activated by the player test")
    @Test
    void invalidActivator() {
        game.getCharacterCards().get(selection).activate(gameManager.getRules(), game);
        game.setRoundOwner(p2);
        action = new DeactivateCard(p2.getNickname(), selection);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Card deactivation test")
    @Test
    void deactivateCard() {
        game.getCharacterCards().get(selection).activate(gameManager.getRules(), game);
        try {
            action.performMove(game, gameManager.getRules());
        } catch(Exception e) {
            fail(e.getMessage());
        }
        assertFalse(game.getCharacterCards().get(selection).isActive());
    }
}