package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivateCardTest {
    GameManager gameManager;
    Player p1, p2, p3;
    Game gameInstance;

    private void initTest() {
        gameManager = new GameManager(new Game());
        p1 = new Player("Ale");
        p2 = new Player("Davide");
        p3 = new Player("Fede");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        gameInstance = gameManager.getGame();
        gameInstance.setRoundOwner(p3);
    }

    @Test
    void canPerformExt() {
        initTest();

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

        choice = 1;
        p3.spendCoins(p3.getBalance());
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertFalse(activateCard.canPerformExt(gameInstance, gameManager.getRules()));
        p3.addCoin();
        p3.addCoin();
        p3.addCoin();

        // Base case, initCharacters() needs to be implemented first
        choice = 1;
        activateCard = new ActivateCard(p3.getNickname(), choice);
        assertTrue(activateCard.canPerformExt(gameInstance, gameManager.getRules()));
    }

    @Test
    void performMove() {
        initTest();
        int cardIndex = 0;
        CharacterCard card = gameInstance.getCharacterCards().get(cardIndex);
        // give money to p3
        for (int i = 0; i < card.getPrice() + 1; i++) {
            gameInstance.incrementPlayerBalance(p3.getNickname());
        }
        Performable action = new ActivateCard(p3.getNickname(), cardIndex);

        int previousPlayerBalance = p3.getBalance();
        int previousGameBalance = gameInstance.getBalance();
        int previousCardPrice = card.getPrice();

        action.performMove(gameInstance, gameManager.getRules());

        assertEquals(previousPlayerBalance - previousCardPrice, p3.getBalance(), "money are removed correctly");
        assertEquals(previousGameBalance + previousCardPrice - 1, gameInstance.getBalance(), "first activation of the card, game get back price - 1");
        assertEquals(previousCardPrice + 1, card.getPrice(), "cost increment on card is right");

        /* check that in the second activation price is manged correctly */
        card.deactivate(gameManager.getRules(), gameInstance);

        // give money to p3
        for (int i = 0; i < card.getPrice() + 1; i++) {
            gameInstance.incrementPlayerBalance(p3.getNickname());
        }

        previousPlayerBalance = p3.getBalance();
        previousGameBalance = gameInstance.getBalance();
        previousCardPrice = card.getPrice();

        action.performMove(gameInstance, gameManager.getRules());

        assertEquals(previousPlayerBalance - previousCardPrice, p3.getBalance(), "money are removed correctly");
        assertEquals(previousGameBalance + previousCardPrice, gameInstance.getBalance(), "second activation of the card, game get back full price");
        assertEquals(previousCardPrice, card.getPrice(), "no cost increment on card is right");

    }

    @Test
    void getNickNamePlayer() {
    }
}