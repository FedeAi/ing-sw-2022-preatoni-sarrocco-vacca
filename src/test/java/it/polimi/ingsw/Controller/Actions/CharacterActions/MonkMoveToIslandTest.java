package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.MonkCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonkMoveToIslandTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    private MonkCharacter card;
    private List<CharacterCard> cardList;
    private int islandIndex;
    private Color selectionColor;
    private int selectionValue;

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
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new MonkCharacter("", game.getBag());
        card.init();
        cardList = new ArrayList<>();
        islandIndex = 0;
        // We need to have at least 1 of a color on the card to activate it, so we cycle through the colors
        selectionColor = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = card.getStudents().getOrDefault(c, 0);
            if (selectionValue > 0) {
                selectionColor = c;
                break;
            }
        }
    }

    @Test
    void canPerformExt() {
        init();
        // First we try to call the underlying Performable abstract
        String wrongNickname = "Suino";
        action = new MonkMoveToIsland(wrongNickname, selectionColor, islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Right name, wrong state
        action = new MonkMoveToIsland(p1.getNickname(), selectionColor, islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Right state, wrong islandIndex
        islandIndex = -1;
        card.activate(gameManager.getRules(), game);
        action = new MonkMoveToIsland(p1.getNickname(), selectionColor, islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        islandIndex = 13;
        action = new MonkMoveToIsland(p1.getNickname(), selectionColor, islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Now we try to have no active card in the list while having a correct islandIndex
        islandIndex = 1;
        action = new MonkMoveToIsland(p1.getNickname(), selectionColor, islandIndex);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Now we're going to have some cards on the list, but not of the MONK type
        KnightCharacter tempCard = new KnightCharacter("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // We're now correctly adding an activated monk
        cardList.add(card);
        // FIXME IF I HAVE MORE THAN 1 CARD ACTIVE, THE CANPERFORM() FAILS
        // THE FIRST CARD IS ACTIVE BUT ISN'T OF THE MONK TYPE
        // assertTrue(action.canPerformExt(game, gameManager.getRules()));

        // Now we remove the other card
        cardList.remove(0);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    void performMove() {
        init();
        islandIndex = 1;
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        game.initCharacterCards(cardList);
        action = new MonkMoveToIsland(p1.getNickname(), selectionColor, islandIndex);
        Island island = game.getIslandContainer().get(islandIndex);
        int initialIslandValue = island.getStudents().getOrDefault(selectionColor, 0);
        action.performMove(game, gameManager.getRules());
        assertEquals(initialIslandValue + 1, island.getStudents().get(selectionColor));
    }
}