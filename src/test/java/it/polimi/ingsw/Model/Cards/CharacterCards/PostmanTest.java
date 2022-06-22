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
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Davide");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        gameManager.initGame();
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