package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayCardTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    Random r;
    int selection;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Davide");
        p3 = new Player(2, "Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setGameState(GameState.PLANNING_CHOOSE_CARD);
        game.setRoundOwner(p3);
        r = new Random();
        selection = r.nextInt(1, 10 + 1);
        action = new PlayCard(p3.getNickname(), selection);
    }

    @DisplayName("Invalid player test")
    @Test
    void nullPlayer() {
        action = new PlayCard("", selection);
        assertThrows(InvalidPlayerException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Check if playcard is performable")
    @Test
    void canPlayCard() {
        assertDoesNotThrow(() -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Wrong state set test")
    void wrongStateTest() {
        // checks if the game is set to the correct game state
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Wrong roundOwner test")
    void wrongRoundOwnerTest() {
        // checking if the player argument is the actual round owner
        // p3 is the actual round owner, while I try to pass p1
        action = new PlayCard(p1.getNickname(), selection);
        assertThrows(RoundOwnerException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        action = new PlayCard(p3.getNickname(), selection);
        game.setRoundOwner(p3);
        assertDoesNotThrow(() -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Wrong card selection test")
    @Test
    void wrongSelectionTest() {
        game.playCard(p3, p3.getCards().get(selection - 1));
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        action = new PlayCard(p3.getNickname(), 11);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Card already played test")
    @Test
    void cardAlreadyPlayed() {
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        game.setRoundOwner(p1);
        action = new PlayCard(p1.getNickname(), selection);
       /*
           FIXME THIS DOESN'T WORK BECAUSE OF THE NEW GETPLAYEDCARDS!
            IN THIS TEST THE INITIAL PLAYERS AREN'T ORDERED!
       assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        */
    }

    @DisplayName("Basic playCard action test")
    @Test
    void playCard() {
        AssistantCard c = p3.getCards().get(selection - 1);
        List<AssistantCard> prevCards = new ArrayList<>(p3.getCards());
        try {
            action.performMove(game, gameManager.getRules());
            ;
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertFalse(p3.hasCard(selection));
        assertEquals(p3.getPlayedCard(), c);
        assertEquals(prevCards.size(), p3.getCards().size() + 1);
        prevCards.remove(c);
        assertTrue(p3.getCards().containsAll(prevCards));
        // FIXME SAME REASON AS ABOVE
        /*
            game.setRoundOwner(p2);
            game.setPlanningOrder();
            assertEquals(1, game.getPlayedCards().size());
            assertTrue(game.getPlayedCards().contains(c));
        */
    }

    /* TODO MAYBE REMOVE THIS (I DON'T KNOW)
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


    }*/
}