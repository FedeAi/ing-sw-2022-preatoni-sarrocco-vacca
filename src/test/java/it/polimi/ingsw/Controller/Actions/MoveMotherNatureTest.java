package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.SuperIsland;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class MoveMotherNatureTest tests the MoveMotherNature action.
 *
 * @author Alessandro Vacca
 * @see MoveMotherNature
 */

class MoveMotherNatureTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable moveMotherNature;
    int movement;
    Color profColor;
    AssistantCard card;


    /**
     * Method init initializes values.
     */
    @BeforeEach
    void init() {
        gameManager = new GameManager();
        p1 = new Player("Ale");
        p2 = new Player("Davide");
        p3 = new Player("Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGameInstance();
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        movement = 3;
        game.setRoundOwner(p3);
        profColor = Color.BLUE;
        moveMotherNature = new MoveMotherNature(p3.getNickname(), movement);
    }

    @Test
    @DisplayName("Wrong nickname test")
    void wrongNicknameTest() {
        // checking if the nickname doesn't belong to the game
        // Nicolò isn't in the game
        moveMotherNature = new MoveMotherNature("Nicolò", movement);
        assertFalse(moveMotherNature.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Wrong roundOwner test")
    void wrongRoundOwnerTest() {
        // checking if the player argument is the actual round owner
        // p2 is the actual round owner, while I try to pass p1
        moveMotherNature = new MoveMotherNature(p1.getNickname(), movement);
        assertFalse(moveMotherNature.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Wrong state set test")
    void wrongStateTest() {
        // checks if the game is set to the correct game state
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertFalse(moveMotherNature.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("playedCard doesn't have enough movement")
    void notEnoughMovementTest() {
        // checks if I have played a card with not enough movement points
        final int cardValue = 1;
        card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);
        assertFalse(moveMotherNature.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Movement not zero")
    void movementNotZeroTest() {
        // checking that I cannot have 0 movements (or negative)
        movement = 0;
        final int cardValue = 1;
        card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);
        moveMotherNature = new MoveMotherNature(p3.getNickname(), movement);
        assertFalse(moveMotherNature.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Basic canPerform test")
    void canPerformExt() {
        // Normal use case
        final int newCardValue = 5;
        card = p3.getCards().stream().filter(c -> c.getValue() == newCardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);
        assertTrue(moveMotherNature.canPerformExt(game, gameManager.getRules()));
    }

    /**
     * Method singleIslandConquer test verifies that after you move motherNature to a certain island
     * and provided you have enough influence, you actually conquer the island.
     */
    @Test
    @DisplayName("Single island conquer")
    void singleIslandConquer() {
        // Simple action test
        game.setProfessor(profColor, game.getRoundOwner().getNickname());
        // Now Fede controls the BLUE professor
        // Next we will be adding some students to the island we're going to go to
        int nextPosition = game.getIslandContainer().correctIndex(movement, game.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        // Then, we play a valid card: 3 movement, we need to play a 5+ value card
        final int cardValue = 5;
        AssistantCard card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);

        moveMotherNature = new MoveMotherNature(game.getRoundOwner().getNickname(), movement);
        moveMotherNature.performMove(game, gameManager.getRules());

        // Checking if motherNature has in fact moved
        assertEquals(nextPosition, game.getMotherNature().getPosition());
        // Now, we check if Fede now controls the island
        int motherNaturePosition = game.getMotherNature().getPosition();
        assertEquals(game.getIslandContainer().get(motherNaturePosition).getOwner(), p3.getNickname());
        // And then we check if the game has correctly changed state
        assertEquals(game.getGameState(), GameState.ACTION_CHOOSE_CLOUD);
    }


    /**
     * The method previousSuperIsland creates the superIsland by first conquering a certain island,
     * then it moves MN by a single position and conquers the island next to the previously conquered one,
     * thus joining the two islands in a superIsland.
     */
    @Test
    @DisplayName("Previous SuperIsland creation")
    void previousSuperIsland() {
        // Simple action test
        game.setProfessor(profColor, game.getRoundOwner().getNickname());
        // Now Fede controls the BLUE professor
        // Next we will be adding some students to the island we're going to go to
        int nextPosition = game.getIslandContainer().correctIndex(movement, game.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        // Then, we play a valid card: 3 movement, we need to play a 5+ value card
        final int cardValue = 5;
        AssistantCard card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);
        // Now we move MN
        moveMotherNature = new MoveMotherNature(game.getRoundOwner().getNickname(), movement);
        moveMotherNature.performMove(game, gameManager.getRules());
        // Checking if motherNature has in fact moved
        assertEquals(nextPosition, game.getMotherNature().getPosition());
        // Now, we check if Fede now controls the island
        int motherNaturePosition = game.getMotherNature().getPosition();
        assertEquals(game.getIslandContainer().get(motherNaturePosition).getOwner(), p3.getNickname());
        // And then we check if the game has correctly changed state
        assertEquals(game.getGameState(), GameState.ACTION_CHOOSE_CLOUD);
        game.setGameState(GameState.ACTION_MOVE_MOTHER);

        // We repeat this for the following one
        movement = 1;
        nextPosition = game.getIslandContainer().correctIndex(movement, game.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        int oldIslands = game.getIslandContainer().size();
        int next = game.getIslandContainer().correctIndex(2, game.getMotherNature().getPosition());
        game.getIslandContainer().get(next).setOwner(p3.getNickname());
        moveMotherNature = new MoveMotherNature(p3.getNickname(), movement);
        moveMotherNature.performMove(game, gameManager.getRules());
        // FIXME THIS SOMETIMES FAILS? MAYBE RELATED TO THE -1 CASE? (MAYBE NOT)
        assertTrue(game.getIslandContainer().get(game.getMotherNature().getPosition()) instanceof SuperIsland);
        assertEquals(game.getIslandContainer().size(), oldIslands - 1);
    }

    /**
     * The method nextSuperIsland creates the superIsland by first conquering a certain island,
     * then it moves MN by 11 positions (the full game board) and conquers the island
     * before the previously conquered one, thus joining the two islands in a superIsland.
     */

    @Test
    @DisplayName("Next SuperIsland creation")
    void nextSuperIsland() {
        // Simple action test
        game.setProfessor(profColor, game.getRoundOwner().getNickname());
        // Now Fede controls the BLUE professor
        // Next we will be adding some students to the island we're going to go to
        int nextPosition = game.getIslandContainer().correctIndex(movement, game.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        // Then, we play a valid card: 3 movement, we need to play a 5+ value card
        final int cardValue = 5;
        AssistantCard card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);
        // Now we move MN
        moveMotherNature = new MoveMotherNature(game.getRoundOwner().getNickname(), movement);
        moveMotherNature.performMove(game, gameManager.getRules());
        // Checking if motherNature has in fact moved
        assertEquals(nextPosition, game.getMotherNature().getPosition());
        // Now, we check if Fede now controls the island
        int motherNaturePosition = game.getMotherNature().getPosition();
        assertEquals(game.getIslandContainer().get(motherNaturePosition).getOwner(), p3.getNickname());
        // And then we check if the game has correctly changed state
        assertEquals(game.getGameState(), GameState.ACTION_CHOOSE_CLOUD);
        game.setGameState(GameState.ACTION_MOVE_MOTHER);

        // We repeat this for the following one
        movement = 11;
        nextPosition = game.getIslandContainer().correctIndex(movement, game.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        int oldIslands = game.getIslandContainer().size();
        moveMotherNature = new MoveMotherNature(p3.getNickname(), movement);
        moveMotherNature.performMove(game, gameManager.getRules());
        assertTrue(game.getIslandContainer().get(game.getMotherNature().getPosition()) instanceof SuperIsland);
        assertEquals(game.getIslandContainer().size(), oldIslands - 1);
    }
}