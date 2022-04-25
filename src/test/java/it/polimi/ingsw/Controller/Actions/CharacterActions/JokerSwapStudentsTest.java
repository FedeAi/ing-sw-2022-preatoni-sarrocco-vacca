package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.JokerCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JokerSwapStudentsTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    Color studentToPick, studentToPut;
    private int selectionValue;
    private JokerCharacter card;
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
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new JokerCharacter("", game.getBag());
        card.init();
        cardList = new LinkedList<>();
        studentToPick = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = card.getStudents().getOrDefault(c, 0);
            if (selectionValue > 0) {
                studentToPick = c;
                break;
            }
        }
        studentToPut = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
                studentToPut = c;
                break;
            }
        }

    }

    @Test
    void canPerformExt() {
        init();
        // First we try to call the underlying Performable abstract
        String wrongNickname = "Suino";
        action = new JokerSwapStudents(wrongNickname, studentToPick, studentToPut);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Now we try to perform the action with the wrong gameState set (set before)
        action = new JokerSwapStudents(p1.getNickname(), studentToPick, studentToPut);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // No cards present check
        card.activate(gameManager.getRules(), game);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // We now have a card present, but not the JOKER
        // It's important also to not have a card that changes the game state
        CharacterCard tempCard = new KnightCharacter("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        tempCard.deactivate(gameManager.getRules(), game);
        // Now we correctly pass the JOKER
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));
        action.performMove(game, gameManager.getRules());
        action.performMove(game, gameManager.getRules());
        action.performMove(game, gameManager.getRules());
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Many branches in the underlying action are quite difficult to take
    }

    @Test
    void performMove() {
        init();
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        game.initCharacterCards(cardList);
        action = new JokerSwapStudents(p1.getNickname(), studentToPick, studentToPut);
        Map<Color, Integer> entry = p1.getSchool().getStudentsEntry();
        int initialStudents = entry.getOrDefault(studentToPick, 0);
        action.performMove(game, gameManager.getRules());
        int finalStudents = entry.get(studentToPick);
        if (studentToPick.equals(studentToPut)) {
            assertEquals(initialStudents, finalStudents);
        } else {
            assertEquals(initialStudents + 1, finalStudents);
        }
    }
}