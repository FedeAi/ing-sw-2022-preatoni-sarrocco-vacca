package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.SuperIsland;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveMotherNatureTest {

    @Test
    void canPerformExt() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Player p2 = new Player("Davide");
        Player p3 = new Player("Fede");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p2);
        Performable moveMotherNature;
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);

        int movement = 3;

        // checking if the nickname doesn't belong to the game
        // Nicolò isn't in the game
        moveMotherNature = new MoveMotherNature("Nicolò", movement);
        assertFalse(moveMotherNature.canPerformExt(gameInstance, gameManager.getRules()));

        // checking if the player argument is the actual round owner
        // p2 is the actual round owner, while I try to pass p1
        moveMotherNature = new MoveMotherNature(p1.getNickname(), movement);
        assertFalse(moveMotherNature.canPerformExt(gameInstance, gameManager.getRules()));

        // checking if the game is set to the correct game state
        moveMotherNature = new MoveMotherNature(p2.getNickname(), movement);
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertFalse(moveMotherNature.canPerformExt(gameInstance, gameManager.getRules()));
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);

        // checking if I have played a card with not enough movement points
        final int cardValue = 1;
        AssistantCard card = p2.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p2.setAndRemovePlayedCard(card);
        assertFalse(moveMotherNature.canPerformExt(gameInstance, gameManager.getRules()));

        // Normal use case
        final int newCardValue = 5;
        card = p2.getCards().stream().filter(c -> c.getValue() == newCardValue).findFirst().get();
        p2.setAndRemovePlayedCard(card);
        assertTrue(moveMotherNature.canPerformExt(gameInstance, gameManager.getRules()));

        // checking that I cannot have 0 movements (or negative)
        movement = 0;
        moveMotherNature = new MoveMotherNature(p2.getNickname(), movement);
        assertFalse(moveMotherNature.canPerformExt(gameInstance, gameManager.getRules()));
    }

    @Test
    void performMove() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Player p2 = new Player("Davide");
        Player p3 = new Player("Fede");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p3);
        Performable moveMotherNature;
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);

        // Simple action test
        int movement = 3;
        Color profColor = Color.BLUE;
        gameInstance.setProfessor(profColor, gameInstance.getRoundOwner().getNickname());
        // Now Fede controls the BLUE professor
        // Next we will be adding some students to the island we're going to go to
        int nextPosition = gameInstance.getIslandContainer().correctIndex(movement, gameInstance.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            gameInstance.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        // Then, we play a valid card: 3 movement, we need to play a 5+ value card
        final int cardValue = 5;
        AssistantCard card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);

        moveMotherNature = new MoveMotherNature(gameInstance.getRoundOwner().getNickname(), movement);
        moveMotherNature.performMove(gameInstance, gameManager.getRules());

        // Checking if motherNature has in fact moved
        assertEquals(nextPosition, gameInstance.getMotherNature().getPosition());
        // Now, we check if Fede now controls the island
        int motherNaturePosition = gameInstance.getMotherNature().getPosition();
        assertEquals(gameInstance.getIslandContainer().get(motherNaturePosition).getOwner(), p3.getNickname());
        // And then we check if the game has correctly changed state
        assertEquals(gameInstance.getGameState(), GameState.ACTION_CHOOSE_CLOUD);
    }

    @Test
    void performMoveSuperIsland() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Player p2 = new Player("Davide");
        Player p3 = new Player("Fede");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p3);
        Performable moveMotherNature;
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);

        // Simple action test
        int movement = 3;
        Color profColor = Color.BLUE;
        gameInstance.setProfessor(profColor, gameInstance.getRoundOwner().getNickname());
        // Now Fede controls the BLUE professor
        // Next we will be adding some students to the island we're going to go to
        int nextPosition = gameInstance.getIslandContainer().correctIndex(movement, gameInstance.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            gameInstance.getIslandContainer().get(nextPosition).addStudent(profColor);
        }

        // Then, we play a valid card: 3 movement, we need to play a 5+ value card
        final int cardValue = 5;
        AssistantCard card = p3.getCards().stream().filter(c -> c.getValue() == cardValue).findFirst().get();
        p3.setAndRemovePlayedCard(card);

        moveMotherNature = new MoveMotherNature(gameInstance.getRoundOwner().getNickname(), movement);
        moveMotherNature.performMove(gameInstance, gameManager.getRules());
        // Checking if motherNature has in fact moved
        assertEquals(nextPosition, gameInstance.getMotherNature().getPosition());
        // Now, we check if Fede now controls the island
        int motherNaturePosition = gameInstance.getMotherNature().getPosition();
        assertEquals(gameInstance.getIslandContainer().get(motherNaturePosition).getOwner(), p3.getNickname());
        // And then we check if the game has correctly changed state
        assertEquals(gameInstance.getGameState(), GameState.ACTION_CHOOSE_CLOUD);
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);

        // We repeat this for the following one
        movement = 1;
        nextPosition = gameInstance.getIslandContainer().correctIndex(movement, gameInstance.getMotherNature().getPosition());
        for (int i = 0; i < 5; i++) {
            gameInstance.getIslandContainer().get(nextPosition).addStudent(profColor);
        }
        int oldIslands = gameInstance.getIslandContainer().size();
        int next = gameInstance.getIslandContainer().correctIndex(2, gameInstance.getMotherNature().getPosition());
        gameInstance.getIslandContainer().get(next).setOwner(p3.getNickname());
        moveMotherNature = new MoveMotherNature(p3.getNickname(), movement);
        moveMotherNature.performMove(gameInstance, gameManager.getRules());
        assertTrue(gameInstance.getIslandContainer().get(gameInstance.getMotherNature().getPosition()) instanceof SuperIsland);
        assertEquals(gameInstance.getIslandContainer().size(), oldIslands - 1);
    }

    @Test
    void getNickNamePlayer() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("Ale");
        Performable moveMotherNature = new MoveMotherNature(p1.getNickname(), 1);
        assertEquals(moveMotherNature.getNickNamePlayer(), p1.getNickname());
    }
}