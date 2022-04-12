package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivateCardTest {

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
        gameInstance.setRoundOwner(p3);
        Performable activateCard;
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);
        int choice;
        choice = 1;
        gameInstance.setExpertMode(true);

        // First, we try to pass a wrong nickname
        String wrongNickname = "NomeSbagliato";
        activateCard = new ActivateCard(wrongNickname, choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));

        // checking if the player argument is the actual round owner
        // p3 is the actual round owner, while I try to pass p1
        activateCard = new ActivateCard(p1.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));

        // checking if the game is set to the correct game state
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));
        gameInstance.setGameState(GameState.ACTION_MOVE_MOTHER);

        /*
         TODO the game logic to check for the expertRules just isn't there yet.
         Also the isActive method is not really testable here
         You would need to first play the card and then retry to use it again
        */

        // Simple test to check for the expert game mode
        gameInstance.setExpertMode(false);
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));
        gameInstance.setExpertMode(true);

        // Then, we see if we have a correct "choice" value
        choice = -1;
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));

        choice = 10;
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));

        // Check if the player is poor (no money left), then restore the balance
        // This is commented out because initCharacters() is needed.
        /*
        choice = 1;
        p3.spendCoins(p3.getBalance());
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));
        p3.addCoin(new Object());
        p3.addCoin(new Object());
        p3.addCoin(new Object());
        */

        // Base case, initCharacters() needs to be implemented first
        choice = 1;
        activateCard = new ActivateCard(p3.getNickname(), choice);
        // assertTrue(activateCard.canPerformExt(gameInstance, gameManager.getRules()));
    }

    @Test
    void performMove() {
    }

    @Test
    void getNickNamePlayer() {
    }
}