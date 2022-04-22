package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PostmanCharacterTest {
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
    void activate() {
        init();
        card.activate(gameManager.getRules(), game);
        assertTrue(gameManager.getRules().getDynamicRules() instanceof PostmanRules, " check rules have been updated");
    }

    @Test
    void deactivate() {
        init();
        card.activate(gameManager.getRules(), game);
        assertTrue(gameManager.getRules().getDynamicRules() instanceof BaseRules, " check rules have been updated");
    }
}