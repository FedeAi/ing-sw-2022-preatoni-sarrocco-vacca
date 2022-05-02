package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayCardTest {

    @Test
    void canPerformExt() throws Exception {

        GameManager gameManager = new GameManager(new Game());
        Player p1 = new Player("fede");
        Player p2 = new Player("gianfranco");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGame();

        gameInstance.setRoundOwner(p1);
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);


        //!* standard execution
        AssistantCard choice = p1.getCards().get(0);
        Performable playCard = new PlayCard("fede", choice);
        assertTrue(playCard.canPerformExt(gameInstance, gameManager.getRules()));

        //wrong game phase
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertFalse(playCard.canPerformExt(gameInstance, gameManager.getRules()));
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);

        // is round owner
        assertTrue(playCard.canPerformExt(gameInstance, gameManager.getRules()));
        gameInstance.setRoundOwner(p2);
        assertFalse(playCard.canPerformExt(gameInstance, gameManager.getRules()));

        // wrong choice
        p1.setAndRemovePlayedCard(choice);
        assertFalse(playCard.canPerformExt(gameInstance, gameManager.getRules()));

        // All player's cards are on table -> he can make that choice
        // todo
    }

    @Test
    void performMove() throws Exception {
        GameManager gameManager = new GameManager(new Game());
        Player p1 = new Player("fede");
        Player p2 = new Player("gianfranco");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGame();

        gameInstance.setRoundOwner(p1);
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        AssistantCard choice = p1.getCards().get(0);
        Performable playCard = new PlayCard("fede", choice);

        // checking player cards
        List<AssistantCard> prev_cards = new ArrayList<>(p1.getCards());

        //!* PERFORM MOVE
        playCard.performMove(gameInstance, gameManager.getRules());

        assertFalse(p1.hasCard(choice));
        assertEquals(p1.getPlayedCard(), choice);
        assertFalse(p1.getCards().contains(choice));
        assertEquals(gameInstance.getRoundOwner(), p2);

        assertEquals(prev_cards.size(), p1.getCards().size() + 1);
        prev_cards.remove(choice);
        assertTrue(p1.getCards().containsAll(prev_cards));

        assertEquals(1, gameInstance.getPlayedCards().size());
        assertTrue(gameInstance.getPlayedCards().contains(choice));
    }

    @Test
    public void setActionOrder() {


        GameManager gameM = new GameManager(new Game());

        Player p1 = new Player("fede");
        Player p2 = new Player("gianfranco");

        gameM.addPlayer(p1);
        gameM.addPlayer(p2);
        gameM.initGame();

        Game gameInstance = gameM.getGame();

        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);

        //p1 move
        gameInstance.setRoundOwner(p1);
        AssistantCard choice = p1.getCards().get(0); //value 1

        Performable playCard1 = new PlayCard("fede", choice);
        playCard1.performMove(gameInstance, gameM.getRules());
        //end p1

        //start p2 move
        gameInstance.setRoundOwner(p2);
        AssistantCard choice2 = p2.getCards().get(1); //value 2
        Performable playCard2 = new PlayCard("gianfranco", choice2);
        playCard2.performMove(gameInstance, gameM.getRules());
        //end p2 move
        //and planning phase


        ArrayList<Player> expected = new ArrayList<>(); //list of player
        expected.add(p1);
        expected.add(p2);

        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);

        assertFalse(!expected.equals(gameInstance.getOrderedPlanningPlayers()));

        //list of expected player should be equals


    }
}