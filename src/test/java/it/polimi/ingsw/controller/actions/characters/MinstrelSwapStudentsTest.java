package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Minstrel;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinstrelSwapStudentsTest {

    Performable action;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;
    Color studentFromEntry, studentFromHall;
    Minstrel card;
    LinkedList<CharacterCard> cardList;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Davide");
        game.createPlayer(2, "Fede");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        p3 = game.getPlayers().get(2);
        gameManager.initGame();
        game.setRoundOwner(p1);
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new Minstrel("");
        card.init();
        cardList = new LinkedList<>();
        cardList.add(card);
        game.initCharacterCards(cardList);
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
        p1.getSchool().addStudentHall(studentFromHall);
        action = new MinstrelSwapStudents(p1.getNickname(), studentFromEntry, studentFromHall);
        card.activate(gameManager.getRules(), game);
    }

    @Test
    @DisplayName("Wrong state test")
    void wrongState() {
        // Now we try to perform the action with the wrong gameState set (set before)
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active minstrels test")
    @Test
    void noMinstrels() {
        game.initCharacterCards(new ArrayList<>());
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Selected student not in the entry test")
    @Test
    void noColorEntry() {
        for (int i = 0; i < 9; i++) {
            p1.getSchool().removeStudentFromEntry(studentFromEntry);
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Selected student not in the hall test")
    @Test
    void noColorHall() {
        // player has not enough students in the hall
        p1.getSchool().moveStudentFromHallToEntry(studentFromHall);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Max color in the hall test")
    @Test
    void maxHall() {
        for (int i = 0; i < Constants.SCHOOL_LANE_SIZE; i++) {
            p1.getSchool().addStudentHall(studentFromEntry);
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Minstrel swap student test")
    @Test
    void minstrelCard() {
        int countEntry = 0, countHall = 0, countPrevEntry = 0, countPrevHall = 0;
        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countPrevEntry += selectionValue;
            }
        }
        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsHall().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countPrevHall += selectionValue;
            }
        }
        Map<Color, Integer> prevEntry = p1.getSchool().getStudentsEntry();
        Map<Color, Integer> prevHall = p1.getSchool().getStudentsHall();
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        Map<Color, Integer> actualEntry = p1.getSchool().getStudentsEntry();
        Map<Color, Integer> actualHall = p1.getSchool().getStudentsHall();

        int nextEntry = prevEntry.getOrDefault(studentFromHall, 0) + 1;
        int nextHall = prevHall.getOrDefault(studentFromEntry, 0) + 1;

        if (studentFromHall == studentFromEntry) { //if equals doesn't change
            nextEntry = prevEntry.getOrDefault(studentFromHall, 0);
            nextHall = prevHall.getOrDefault(studentFromEntry, 0);
            assertEquals(prevEntry, actualEntry);
            assertEquals(prevHall, actualHall);
        }

        assertEquals(nextEntry, actualEntry.get(studentFromHall), "correctness of swap");
        assertEquals(nextHall, actualHall.get(studentFromEntry), "correctness of swap");

        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countEntry += selectionValue;
            }
        }

        for (Color c : Color.values()) {
            int selectionValue = p1.getSchool().getStudentsHall().getOrDefault(c, 0);
            if (selectionValue > 0) {
                countHall += selectionValue;
            }
        }
        assertEquals(countPrevEntry, countEntry, "correct size entry");
        assertEquals(countPrevHall, countHall, "correct size hall");
    }
}