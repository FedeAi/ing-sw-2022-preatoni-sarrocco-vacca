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

        //action.performMove(game, gameManager.getRules());
        //action.performMove(game, gameManager.getRules());
        /*
         *   FIXME the assert sometimes fails, I think it's caused by a failed performMove
         *    I.E. if I have only one student from either hall or entry (in this case the random one is entry),
         *    the next action is going to fail, since the number of students is zero
         *    the canPerform WILL fail, and the action won't move any students
         */
        // assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Many branches in the underlying action are quite difficult to take
    }

    @Test
    void performMove() {
        init();
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        //1st check the number of entry
        action = new MinstrelSwapStudents(p1.getNickname(), studentFromEntry, studentFromHall);
        int prevEntry = game.getRoundOwner().getSchool().getStudentsEntry().size();
        action.performMove(game, gameManager.getRules());   // Guarda che fallisce la can perform
        int postEntry = game.getRoundOwner().getSchool().getStudentsEntry().size();
        assertEquals(prevEntry, postEntry);
        //2nd check the number of hall
        int prevHall = game.getRoundOwner().getSchool().getStudentsHall().size();
        action.performMove(game, gameManager.getRules());
        int postHall = game.getRoundOwner().getSchool().getStudentsHall().size();
        assertEquals(prevHall, postHall);
        //check the color
        if (studentFromEntry.equals(studentFromHall)) { //if the colors are equals then swap doesn't change
            int prevColor = game.getRoundOwner().getSchool().getStudentsEntry().get(studentFromEntry);
            action.performMove(game, gameManager.getRules());
            int postColor = game.getRoundOwner().getSchool().getStudentsEntry().get(studentFromHall);
            assertEquals(prevColor, postColor);
        } else {
            // if the colors are different each other, then the prevColor of entry prevColor = postColor -1
            //                                         then the preColor od Hall prevColor = postColor +1;
            //1st checking the colors of entry
            int prevColor1 = game.getRoundOwner().getSchool().getStudentsEntry().get(studentFromEntry);
            int prevColor2 = game.getRoundOwner().getSchool().getStudentsEntry().get(studentFromHall);
            action.performMove(game, gameManager.getRules());
            int postColor1 = game.getRoundOwner().getSchool().getStudentsEntry().getOrDefault(studentFromEntry, 0);
            int postColor2 = game.getRoundOwner().getSchool().getStudentsEntry().getOrDefault(studentFromHall, 0);
            // TODO
            /**
             something doesn't work, pls don't touch i want to fix it
             */
            //  assertEquals(prevColor1,postColor1-1);
            //assertEquals(prevColor2,postColor2+1);
            //2nd like before but for Hall
           /* int prevColor11 = game.getRoundOwner().getSchool().getStudentsHall().get(studentToPut);
            int prevColor21 = game.getRoundOwner().getSchool().getStudentsHall().get(studentToPick);
            action.performMove(game, gameManager.getRules());
            int postColor12 = game.getRoundOwner().getSchool().getStudentsHall().get(studentToPut);
            int postColor22 = game.getRoundOwner().getSchool().getStudentsHall().get(studentToPick);
            assertEquals(prevColor11,postColor12);
            assertEquals(prevColor21,postColor22);
            */

        }
    }

    @Test
    void performMoveProposta() {
        init();
        card.activate(gameManager.getRules(), game);
        cardList.add(card);
        game.initCharacterCards(cardList);
        action = new MinstrelSwapStudents(p1.getNickname(), studentFromEntry, studentFromHall);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));

        EnumMap<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p1.getSchool().getStudentsEntry());
        EnumMap<Color, Integer> prevHall = new EnumMap<Color, Integer>(p1.getSchool().getStudentsHall());
        action.performMove(game, gameManager.getRules());
        EnumMap<Color, Integer> actualEntry = new EnumMap<Color, Integer>(p1.getSchool().getStudentsEntry());
        EnumMap<Color, Integer> actualHall = new EnumMap<Color, Integer>(p1.getSchool().getStudentsHall());

        int nextEntry = prevEntry.getOrDefault(studentFromHall, 0) + 1;
        int nextHall = prevHall.getOrDefault(studentFromEntry, 0) + 1;

        if (studentFromHall == studentFromEntry) {
            nextEntry = prevEntry.getOrDefault(studentFromHall, 0);
            nextHall = prevHall.getOrDefault(studentFromEntry, 0);
        }


        assertEquals(nextEntry, actualEntry.get(studentFromHall), "correctness of swap");
        assertEquals(nextHall, actualHall.get(studentFromEntry), "correctness of swap");

        assertEquals(prevEntry.size(), actualEntry.size(), "entry size not changed");
        assertEquals(prevHall.size(), actualHall.size(), "hall size not changed");
    }

}