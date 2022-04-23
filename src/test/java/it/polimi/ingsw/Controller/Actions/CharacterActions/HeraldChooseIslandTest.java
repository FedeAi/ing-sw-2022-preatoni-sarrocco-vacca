package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CentaurCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.HeraldCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class HeraldChooseIslandTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    private int islandIndex;
    private CharacterCard card;
    private LinkedList<CharacterCard> cardList;
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
        game.setRoundOwner(p1);
    }

    @Test
    void canPerformExt() {
        init();
        islandIndex = 1;
        // First we try to call the underlying Performable abstract
        String wrongNickname = "Achille";
        action = new HeraldChooseIsland(wrongNickname, islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Now we try to perform the action with the wrong gameState set
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // We activate the card, without adding it to the active cards
        card = new HeraldCharacter("");
        card.activate(gameManager.getRules(), game);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Then we try to reach the semi-impossible case of being in HERALD_ACTIVE state with the wrong card
        // after we remove the wrong card, we then add the right one
        card = new CentaurCharacter("");
        card.activate(gameManager.getRules(), game);
        cardList = new LinkedList<>();
        cardList.add(card);
        game.initCharacterCards(cardList);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        cardList.remove(0);
        card.deactivate(gameManager.getRules(), game);
        card = new HeraldCharacter("");
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        // We then try to check if the action can be performed with a wrong island index;
        islandIndex = 13;
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        islandIndex = -1;
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // After all of this, we now set everything to their correct values
        islandIndex = 1;
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    void performMove() {
        init();
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        Color profColor = Color.BLUE;
        game.setProfessor(profColor, game.getRoundOwner().getNickname());
        // Now Ale controls the BLUE professor
        // Next we will be adding some students to the island we're going to trigger the card on
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(islandIndex).addStudent(profColor);
        }
        // Card trigger
        action.performMove(game, gameManager.getRules());
        assertEquals(p1.getNickname(), game.getIslandContainer().get(islandIndex).getOwner());
        // We now try to conquer another island in order to join it with the previous one
        islandIndex++;
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(islandIndex).addStudent(profColor);
        }
        // Card trigger, and the current islandIndex will be joined with the previous one
        action.performMove(game, gameManager.getRules());
        assertEquals(p1.getNickname(), game.getIslandContainer().get(islandIndex - 1).getOwner());
        // Now we join the island with the next one
        islandIndex = 0;
        action = new HeraldChooseIsland(p1.getNickname(), islandIndex);
        for (int i = 0; i < 5; i++) {
            game.getIslandContainer().get(islandIndex).addStudent(profColor);
        }
        // Card trigger, and the current islandIndex will be joined with the previous one
        action.performMove(game, gameManager.getRules());
        assertEquals(p1.getNickname(), game.getIslandContainer().get(islandIndex).getOwner());
    }
}