package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    private PostmanCharacter card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2;

    private void init() {
        card = new PostmanCharacter("");

        gameManager = new GameManager();
        p1 = new Player("DraghettoMagico");
        p2 = new Player("FatinaBullone");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        game = gameManager.getGameInstance();
    }


    @Test
    void isActive() {
        init();
        assertFalse(card.isActive(), "check that the card is not randomly active");
        card.activate(gameManager.getRules(), game);
        assertTrue(card.isActive(), "check active");
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "check active");
    }

    @Test
    void getPrice() {
        init();
        assertNotNull(card.getPrice(), "check CardPrice");
        assertTrue(card.getPrice() > 0, "check CardPrice");
        int initialPrice = card.getPrice();
        card.activate(gameManager.getRules(), game);
        assertEquals(card.getPrice(), initialPrice + 1, "check price increment");
        card.deactivate(gameManager.getRules(), game);
        assertEquals(card.getPrice(), initialPrice + 1, "check price increment is kept");
        card.activate(gameManager.getRules(), game);
        assertEquals(card.getPrice(), initialPrice + 1, "check price increment is not done the second time");
    }
}