package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.controller.actions.MoveMotherNature;
import it.polimi.ingsw.controller.actions.MoveStudentFromEntryToHall;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Grandma;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class GrandmaBlockIslandTest tests the Grandma character (island blocking feature)
 * This also tests the effects of the card regarding a normal MoveMotherNature action.
 *
 * @author Alessandro Vacca
 * @see GrandmaBlockIsland
 * @see MoveMotherNature
 */

class GrandmaBlockIslandTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    Grandma grandma;
    List<CharacterCard> cardList;
    int selectedIsland;


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
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        game.setRoundOwner(p1);
        selectedIsland = game.getIslandContainer().correctIndex(1, game.getMotherNature().getPosition());
        grandma = new Grandma("");
        cardList = new ArrayList<>();
        cardList.add(grandma);
        game.initCharacterCards(cardList);
        grandma.activate(gameManager.getRules(), game);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
    }

    @Test
    @DisplayName("Wrong state test")
    void wrongState() {
        // We aren't set to the correct state
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Wrong islandIndex")
    void wrongIndex() {
        // Negative selectedIsland test
        selectedIsland = -1;
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
        // selectedIsland >= islands.size() test
        selectedIsland = game.getIslandContainer().size();
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertThrows(InvalidIndexException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Already blocked island")
    void alreadyBlocked() {
        // We set the selectedIsland to blocked, we then try to block it
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
        // I cannot block the same island 2 times!
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("No grandmas in the game test")
    void noGrandmas() {
        game.initCharacterCards(new ArrayList<>());
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("No active grandmas test")
    void grandmaInactive() {
        grandma.deactivate(gameManager.getRules(), game);
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("No blockingCards left")
    void blockingUnavailable() {
        // We now empty the grandma card
        while (grandma.getBlockingCards() != 0) {
            grandma.moveBlockingCard();
        }
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @Test
    @DisplayName("Grandma blocking selection")
    void grandmaSelection() {
        grandma.activate(gameManager.getRules(), game);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(game.getIslandContainer().get(selectedIsland).isBlocked());
    }

    @Test
    @DisplayName("Checks if the island is blocked")
    void influenceBlocked() {
        // We activate the card, block the island, and then see if the ownership doesn't get updated
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        Color student = Color.PINK;
        // P1 will conquer the PINK professor
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        p1.getSchool().addStudentEntry(student);
        action = new MoveStudentFromEntryToHall(p1.getNickname(), student);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        for (int i = 0; i < 6; i++) {
            game.getIslandContainer().addIslandStudent(selectedIsland, student);
        }
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        int movement = 1;
        p1.setAndRemovePlayedCard(new AssistantCard("", 10));
        action = new MoveMotherNature(p1.getNickname(), movement);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        Island island = game.getIslandContainer().get(selectedIsland);
        assertFalse(island.isBlocked());
        assertNotEquals(p1.getNickname(), island.getOwner());
    }
}