package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Joker;
import it.polimi.ingsw.model.cards.characters.Knight;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method JokerSwapStudentsTest tests the JokerSwapStudents action.
 *
 * @see JokerSwapStudents
 */
class JokerSwapStudentsTest {

    Performable action;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;
    Color studentCard, studentEntry;
    int selectionValue;
    Joker card;
    LinkedList<CharacterCard> cardList;

    /**
     * Method init initializes values.
     */
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
        card = new Joker(game.getBag());
        card.init();
        cardList = new LinkedList<>();
        studentCard = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = card.getStudentsMap().getOrDefault(c, 0);
            if (selectionValue > 0) {
                studentCard = c;
                break;
            }
        }
        studentEntry = Color.BLUE;
        for (Color c : Color.values()) {
            selectionValue = p1.getSchool().getStudentsEntry().getOrDefault(c, 0);
            if (selectionValue > 0) {
                studentEntry = c;
                break;
            }
        }
        action = new JokerSwapStudents(p1.getNickname(), studentCard, studentEntry);
    }

    /**
     * Method wrongState tests if an action is created with the wrong state set.
     * JokerSwapStudents action can only be performed in the Joker state.
     */
    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method noActives checks if there are no active character cards.
     */
    @DisplayName("No active cards test")
    @Test
    void noActives() {
        // No cards present check
        card.activate(gameManager.getRules(), game);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method noJokers checks if there are no Jokers in the current game.
     */
    @DisplayName("No active jokers test")
    @Test
    void noJokers() {
        // We now have a card present, but not the JOKER
        // It's important also to not have a card that changes the game state
        card.activate(gameManager.getRules(), game);
        CharacterCard tempCard = new Knight();
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method maxSwaps checks if a player has already done his maximum swaps.
     */
    @DisplayName("Max swaps test")
    @Test
    void maxSwaps() {
        cardList.add(card);
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        for (int i = 0; i < 3; i++) {
            p1.getSchool().addStudentEntry(studentEntry);
            try {
                action.performMove(game, gameManager.getRules());
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }
        game.setGameState(GameState.JOKER_SWAP_STUDENTS);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    /**
     * Method jokerCard tests a valid JokerSwapStudents action,
     * checking if the students have been swapped after the action.
     */
    @DisplayName("Joker swap students test")
    @Test
    void jokerCard() {
        Map<Color, Integer> entry = p1.getSchool().getStudentsEntry();
        int initialStudents = entry.getOrDefault(studentCard, 0);
        cardList.add(card);
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        int finalStudents = p1.getSchool().getStudentsEntry().get(studentCard);
        if (studentCard.equals(studentEntry)) {
            assertEquals(initialStudents, finalStudents);
        } else {
            assertEquals(initialStudents + 1, finalStudents);
        }
    }
}