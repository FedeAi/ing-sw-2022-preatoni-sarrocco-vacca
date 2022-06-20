package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
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
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Fede");
        p3 = new Player(2, "Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        game = gameManager.getGame();
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