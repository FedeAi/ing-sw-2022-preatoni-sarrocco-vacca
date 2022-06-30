package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.controller.rules.dynamic.FarmerRules;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method FarmerTest tests the Farmer class.
 *
 * @see Farmer
 */
class FarmerTest {

    Farmer card;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;

    /**
     * Method init initializes the values needed for the test.
     */
    @BeforeEach
     void init() {
        card = new Farmer("");
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
     * Method activate tests the activation of the Farmer Character card.
     */
    @Test
    @DisplayName("Farmer activation test")
    void activate() {
        card.activate(gameManager.getRules(), game);
        assertTrue(card.isActive(), "Checks if the active flag has been set to true");
        assertTrue(gameManager.getRules().getDynamicRules() instanceof FarmerRules, "Checks if rules have been updated");
    }

    /**
     * Method deactivate tests the deactivation of the Farmer Character card.
     */
    @Test
    @DisplayName("Farmer deactivation test")
    void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");
        assertFalse(gameManager.getRules().getDynamicRules() instanceof FarmerRules, "Checks if rules have been set to base");
    }
}