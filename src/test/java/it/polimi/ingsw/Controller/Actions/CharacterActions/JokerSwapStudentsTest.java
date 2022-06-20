package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Joker;
import it.polimi.ingsw.Model.Cards.CharacterCards.Knight;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JokerSwapStudentsTest {

    Performable action;
    Game game;
    GameManager gameManager;
    Player p1, p2, p3;
    Color studentCard, studentEntry;
    int selectionValue;
    Joker card;
    LinkedList<CharacterCard> cardList;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        p1 = new Player(0, "Ale");
        p2 = new Player(1, "Fede");
        p3 = new Player(2, "Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setRoundOwner(p1);
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        card = new Joker("", game.getBag());
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

    @DisplayName("Wrong state test")
    @Test
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active cards test")
    @Test
    void noActives() {
        // No cards present check
        card.activate(gameManager.getRules(), game);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active jokers test")
    @Test
    void noJokers() {
        // We now have a card present, but not the JOKER
        // It's important also to not have a card that changes the game state
        card.activate(gameManager.getRules(), game);
        CharacterCard tempCard = new Knight("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    // FIXME IS THIS AN IMPOSSIBLE CASE ? this is the same for a lot of stuff
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