package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.PrincessCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrincessMoveToEntryTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    private PrincessCharacter card;
    private List<CharacterCard> cardList;
    private Color selectionColor;
    private int selectionValue;

    private void init() {

        gameManager = new GameManager(new Game());
        p1 = new Player("Ale");
        p2 = new Player("Fede");
        p3 = new Player("Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setRoundOwner(p1);
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new PrincessCharacter("", game.getBag());
        card.init();
        cardList = new ArrayList<>();

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
        String wrongNickname = "Marule";
        action = new PrincessMoveToEntry(wrongNickname, selectionColor);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Right name, wrong state
        action = new PrincessMoveToEntry(p1.getNickname(), selectionColor);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));

        // Now we try to have no active card in the list while having a correct islandIndex
        action = new PrincessMoveToEntry(p1.getNickname(), selectionColor);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Now we're going to have some cards on the list, but not of the Princess type
        KnightCharacter tempCard = new KnightCharacter("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        assertFalse(action.canPerformExt(game, gameManager.getRules()), "there is no princessCharacter in Game");
        /**
         like Monk, to fix when getActiveCharacter will return not just 1 elem
         */
        cardList.remove(0);
        cardList.add(card);
        card.activate(gameManager.getRules(), game);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    void performMove() {
        init();
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        game.initCharacterCards(cardList);
        action = new PrincessMoveToEntry(p1.getNickname(), selectionColor);
        int prevHall = p1.getSchool().getStudentsHall().getOrDefault(selectionColor, 0);
        action.performMove(game, gameManager.getRules());
        assertEquals(prevHall + 1, p1.getSchool().getStudentsHall().getOrDefault(selectionColor, 0));
    }
}
