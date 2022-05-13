package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.GrandmaCharacter;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
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
    GrandmaCharacter grandma;
    List<CharacterCard> cardList;
    int selectedIsland;


    /**
     * Method init initializes values.
     */
    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game());
        p1 = new Player("Ale");
        p2 = new Player("Davide");
        p3 = new Player("Fede");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        game.setRoundOwner(p1);
        selectedIsland = 0;
        grandma = new GrandmaCharacter("");
        cardList = new ArrayList<>();
        cardList.add(grandma);
        game.initCharacterCards(cardList);
    }

    @Test
    @DisplayName("Wrong nickname")
    void superTest() {
        String wrongNickname = "Beppe";
        action = new GrandmaBlockIsland(wrongNickname, selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Wrong state")
    void wrongState() {
        // We aren't set to the correct state
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Wrong islandIndex")
    void wrongIndex() {
        // Negative selectedIsland test
        grandma.activate(gameManager.getRules(), game);
        selectedIsland = -1;
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
        // selectedIsland > islands.size() test
        selectedIsland = 12;
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Already blocked island")
    void alreadyBlocked() {
        // We set the selectedIsland to blocked, we then try to block it
        grandma.activate(gameManager.getRules(), game);
        game.getIslandContainer().get(selectedIsland).setBlocked(true);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Zero cards are active")
    void zeroActives() {
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("No grandmas present")
    void noGrandmas() {
        cardList = new ArrayList<>();
        KnightCharacter tempCard = new KnightCharacter("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        cardList.add(new KnightCharacter(""));
        cardList.add(new KnightCharacter(""));
        game.initCharacterCards(cardList);
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Grandma not active")
    void grandmaInactive() {
        cardList = new ArrayList<>();
        KnightCharacter tempCard = new KnightCharacter("");
        tempCard.activate(gameManager.getRules(), game);
        cardList.add(tempCard);
        cardList.add(grandma);
        game.initCharacterCards(cardList);
        game.setGameState(GameState.GRANDMA_BLOCK_ISLAND);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("No blockingCards left")
    void blockingUnavailable() {
        grandma.activate(gameManager.getRules(), game);
        // We now empty the grandma card
        while (grandma.getBlockingCards() != 0) {
            grandma.moveBlockingCard();
        }
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertFalse(action.canPerform(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Grandma blocking selection")
    void grandmaSelection() {
        grandma.activate(gameManager.getRules(), game);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        assertTrue(action.canPerform(game, gameManager.getRules()));
        action.performMove(game, gameManager.getRules());
        assertTrue(game.getIslandContainer().get(selectedIsland).isBlocked());
    }

    @Test
    @DisplayName("Checks if the island is blocked")
    void influenceBlocked() {
        // We activate the card, block the island, and then see if the ownership doesn't get updated
        grandma.activate(gameManager.getRules(), game);
        action = new GrandmaBlockIsland(p1.getNickname(), selectedIsland);
        action.performMove(game, gameManager.getRules());
        Color student = Color.PINK;
        game.setProfessor(student, p1.getNickname());
        for (int i = 0; i < 6; i++) {
            game.getIslandContainer().get(selectedIsland).addStudent(student);
        }
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        int movement = game.getIslandContainer().size() - game.getMotherNature().getPosition();
        action = new MoveMotherNature(p1.getNickname(), movement);
        action.performMove(game, gameManager.getRules());
        Island island = game.getIslandContainer().get(selectedIsland);
        assertFalse(island.isBlocked());
        assertNotEquals(p1.getNickname(), island.getOwner());
    }
}