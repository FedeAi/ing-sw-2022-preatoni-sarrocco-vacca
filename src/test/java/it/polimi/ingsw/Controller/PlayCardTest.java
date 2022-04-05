package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Actions.PlayCard;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayCardTest {

    @Test
    void canPerformExt() throws Exception{

        GameManager gameManager = new GameManager();
        Player p1 = new Player("fede");
        Player p2 = new Player("gianfranco");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();

        gameInstance.setRoundOwner(p1);
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);



        //!* standard execution
        AssistantCard choice = p1.getCards().get(0);
        Performable playCard = new PlayCard("fede", choice);
        assertTrue(playCard.canPerformExt(gameInstance));

        //wrong game phase
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertFalse(playCard.canPerformExt(gameInstance));
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        // is round owner
        assertTrue(playCard.canPerformExt(gameInstance));
        gameInstance.setRoundOwner(p2);
        assertFalse(playCard.canPerformExt(gameInstance));

        // wrong choice
        p1.setAndRemovePlayedCard(choice);
        assertFalse(playCard.canPerformExt(gameInstance));

        // All player's cards are on table -> he can make that choice
        // todo
    }

    @Test
    void performMove() throws Exception{
        GameManager gameManager = new GameManager();
        Player p1 = new Player("fede");
        Player p2 = new Player("gianfranco");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();

        gameInstance.setRoundOwner(p1);
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        AssistantCard choice = p1.getCards().get(0);
        Performable playCard = new PlayCard("fede", choice);

        // checking player cards
        List<AssistantCard> prev_cards = new ArrayList<>(p1.getCards());

        //!* PERFORM MOVE
        playCard.performMove(gameInstance);

        assertFalse(p1.hasCard(choice));
        assertEquals(p1.getPlayedCard(), choice);
        assertFalse(p1.getCards().contains(choice));
        assertEquals(gameInstance.getRoundOwner(), p2);

        assertEquals(prev_cards.size(), p1.getCards().size()+1);
        prev_cards.remove(choice);
        assertTrue(p1.getCards().containsAll(prev_cards));

        assertEquals(1, gameInstance.getPlayedCards().size());
        assertTrue(gameInstance.getPlayedCards().contains(choice));
    }
}