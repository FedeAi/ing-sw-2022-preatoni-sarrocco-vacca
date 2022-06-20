package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostmanTest {
    private Postman card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2;

    @BeforeEach
    void init() {
        card = new Postman("");

        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        game = gameManager.getGame();
    }

    @Test
    void activate() {
        card.activate(gameManager.getRules(), game);
        assertTrue(gameManager.getRules().getDynamicRules() instanceof PostmanRules, " check rules have been updated");
    }

    @Test
    void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");
        assertFalse(gameManager.getRules().getDynamicRules() instanceof PostmanRules, "Checks if rules have been set to base");
    }
}