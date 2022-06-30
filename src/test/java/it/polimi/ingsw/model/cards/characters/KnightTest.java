package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.controller.rules.dynamic.KnightRules;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method KnightTest tests the Knight class.
 *
 * @see Knight
 */
class KnightTest {

    private Knight card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;

    /**
     * Method init initializes the values needed for the test.
     */
    @BeforeEach
     void init() {
        card = new Knight();
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Davide");
        game.createPlayer(2, "Fede");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        p3 = game.getPlayers().get(2);
        gameManager.initGame();
    }

    /**
     * Method activate tests the activation of the Knight Character card.
     */
    @Test
    @DisplayName("Knight activation test")
    void activate() {
        card.activate(gameManager.getRules(), game);
        assertTrue(card.isActive(), "Checks if the active flag has been set to true");
        assertTrue(gameManager.getRules().getDynamicRules() instanceof KnightRules, "Checks if rules have been updated");
    }

    /**
     * Method deactivate tests the deactivation of the Knight Character card.
     */
    @Test
    @DisplayName("Knight activation test")
    void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");
        assertFalse(gameManager.getRules().getDynamicRules() instanceof KnightRules, "Checks if rules have been set to base");
    }
}