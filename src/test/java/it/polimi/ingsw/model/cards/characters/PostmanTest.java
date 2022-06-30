package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.controller.rules.dynamic.PostmanRules;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Method PostmanTest tests the Postman class.
 *
 * @see Postman
 */
class PostmanTest {
    private Postman card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2;

    /**
     * Method init initializes the values needed for the test.
     */
    @BeforeEach
    void init() {
        card = new Postman();
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Davide");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        gameManager.initGame();
    }

    /**
     * Method activate tests the activation of the Postman Character card.
     */
    @Test
    @DisplayName("Postman activation test")
    void activate() {
        card.activate(gameManager.getRules(), game);
        assertTrue(gameManager.getRules().getDynamicRules() instanceof PostmanRules, " check rules have been updated");
    }

    /**
     * Method deactivate tests the deactivation of the Postman Character card.
     */
    @Test
    @DisplayName("Minstrel deactivation test")
    void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");
        assertFalse(gameManager.getRules().getDynamicRules() instanceof PostmanRules, "Checks if rules have been set to base");
    }
}