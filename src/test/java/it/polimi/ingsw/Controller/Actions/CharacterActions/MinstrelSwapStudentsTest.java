package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.MinstrelCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MinstrelSwapStudentsTest {

    private Performable action;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    Color studentFromEntry, studentFromHall;
    private MinstrelCharacter card;
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
        card = new MinstrelCharacter("");
        card.init();
        cardList = new LinkedList<>();

        studentFromEntry = Color.BLUE;

        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
                studentFromEntry = c;
                break;
            }
        }

        studentFromHall = Color.BLUE;

        // add students to player hall
        p1.getSchool().getStudentsHall().put(studentFromHall, 1);


    }

    @Test
    void canPerformExt() {
        init();
        // First we try to call the underlying Performable abstract
        String wrongNickname = "Scrofa";
        action = new MinstrelSwapStudents(wrongNickname, studentFromEntry, studentFromHall);
        assertFalse(action.canPerformExt(game, gameManager.getRules()), "wrongNickname");

        // Now we try to perform the action with the wrong gameState set (set before)
        action = new MinstrelSwapStudents(p1.getNickname(), studentFromEntry, studentFromHall);
        assertFalse(action.canPerformExt(game, gameManager.getRules()), " wrong gameState");

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
        // Now we correctly pass the MINSTREL
        for (int i = 0; i < cardList.size(); i++) {
            cardList.remove(i);
        }

        cardList.add(card);
        card.activate(gameManager.getRules(), game);
        action = new MinstrelSwapStudents(p1.getNickname(), studentFromEntry, studentFromHall);

        assertTrue(action.canPerformExt(game, gameManager.getRules()));

        // player has not enough students in the hall
        p1.getSchool().getStudentsHall().put(studentFromHall, 0);
        assertFalse(action.canPerformExt(game, gameManager.getRules()), "not enough students in the hall");

        p1.getSchool().getStudentsHall().put(studentFromHall, 1);
        p1.getSchool().getStudentsEntry().put(studentFromEntry, 0);
        assertFalse(action.canPerformExt(game, gameManager.getRules()), "not enough students in the entry");
        assertFalse(action.canPerformExt(game, gameManager.getRules()));


    }

    @Test
    void performMove() {

        int countEntry  = 0, countHall = 0, countPrevEntry = 0, countPrevHall = 0;

        init();
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        game.initCharacterCards(cardList);
        action = new MinstrelSwapStudents(p1.getNickname(), studentFromEntry, studentFromHall);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));


        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countPrevEntry +=selectionValue;
            }
        }


        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsHall().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countPrevHall +=selectionValue;
            }
        }
        EnumMap<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p1.getSchool().getStudentsEntry());
        EnumMap<Color, Integer> prevHall = new EnumMap<Color, Integer>(p1.getSchool().getStudentsHall());
        action.performMove(game, gameManager.getRules());
        EnumMap<Color, Integer> actualEntry = new EnumMap<Color, Integer>(p1.getSchool().getStudentsEntry());
        EnumMap<Color, Integer> actualHall = new EnumMap<Color, Integer>(p1.getSchool().getStudentsHall());

        int nextEntry = prevEntry.getOrDefault(studentFromHall, 0) + 1;
        int nextHall = prevHall.getOrDefault(studentFromEntry, 0) + 1;

        if (studentFromHall == studentFromEntry) { //if equals doesn't change
            nextEntry = prevEntry.getOrDefault(studentFromHall, 0);
            nextHall = prevHall.getOrDefault(studentFromEntry, 0);
            assertEquals(prevEntry,actualEntry);
            assertEquals(prevHall,actualHall);
        }

        assertEquals(nextEntry, actualEntry.get(studentFromHall), "correctness of swap");
        assertEquals(nextHall, actualHall.get(studentFromEntry), "correctness of swap");

        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
               countEntry +=selectionValue;
            }
        }


        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsHall().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countHall +=selectionValue;
            }
        }
        assertEquals(countPrevEntry, countEntry, "correct size entry");
        assertEquals(countPrevHall, countHall, "correct size hall");

        action.performMove(game, gameManager.getRules());

    }


}