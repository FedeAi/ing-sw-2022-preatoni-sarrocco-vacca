package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.DynamicRules.KnightRules;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnightCharacterTest {

    private KnightCharacter card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;

    @BeforeEach
     void init() {
        card = new KnightCharacter("");
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Fede");
        p3 = new Player(2, "Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
    }

    @Test
    void activate() {
        card.activate(gameManager.getRules(), game);
        assertTrue(card.isActive(), "Checks if the active flag has been set to true");
        assertTrue(gameManager.getRules().getDynamicRules() instanceof KnightRules, "Checks if rules have been updated");
    }

    @Test
    void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");
        assertFalse(gameManager.getRules().getDynamicRules() instanceof KnightRules, "Checks if rules have been set to base");
    }
}