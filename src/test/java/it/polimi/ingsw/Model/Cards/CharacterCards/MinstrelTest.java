package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinstrelTest {

    private Minstrel card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;

    @BeforeEach
    void init() {
        card = new Minstrel("");
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
        init();
        card.activate(gameManager.getRules(), game);
        assertTrue(card.isActive(), "Checks if the active flag has been set to true");
    }

    @Test
    void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");
    }
}
