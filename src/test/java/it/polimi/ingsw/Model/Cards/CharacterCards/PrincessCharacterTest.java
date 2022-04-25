package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.DynamicRules.KnightRules;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrincessCharacterTest {

    private PrincessCharacter card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;

    private void init() {
        gameManager = new GameManager();
        p1 = new Player("Ale");
        p2 = new Player("Fede");
        p3 = new Player("Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGameInstance();
        card = new PrincessCharacter("", game.getBag());
    }

    @Test
    public void activate() {
        init();
        card.activate(gameManager.getRules(), game);
        assertTrue(card.isActive(), "Checks if the active flag has been set to true");
    }

    @Test
    public void deactivate() {
        activate();
        card.deactivate(gameManager.getRules(), game);
        assertFalse(card.isActive(), "Checks if the active flag has been set to false");

    }
}
