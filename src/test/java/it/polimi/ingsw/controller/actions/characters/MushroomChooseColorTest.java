package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.controller.actions.MoveMotherNature;
import it.polimi.ingsw.controller.actions.MoveStudentFromEntryToHall;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Knight;
import it.polimi.ingsw.model.cards.characters.Mushroom;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class MushroomChooseColorTest tests the MushroomChooseColor action.
 *
 * @author Alessandro Vacca
 * @see MushroomChooseColor
 */
class MushroomChooseColorTest {

    GameManager gameManager;
    Player p1, p2, p3;
    Game game;
    Performable action;
    CharacterCard card;
    List<CharacterCard> cardList;
    Color student;
    int index;

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
        card = new Mushroom("");
        cardList = new ArrayList<>();
        cardList.add(card);
        game.initCharacterCards(cardList);
        index = game.getIslandContainer().correctIndex(1, game.getMotherNature().getPosition());
        student = game.getIslandContainer().get(index).getStudents().entrySet().stream().filter((s) -> s.getValue() > 0).findFirst().get().getKey();
        action = new MushroomChooseColor(p1.getNickname(), student);
    }

    @Test
    @DisplayName("Wrong state")
    void wrongState() {
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        assertThrows(WrongStateException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active mushrooms test")
    @Test
    void noMushrooms() {
        // We now have a card present, but not the JOKER
        // It's important also to not have a card that changes the game state
        card.activate(gameManager.getRules(), game);
        CharacterCard tempCard = new Knight("");
        tempCard.activate(gameManager.getRules(), game);
        cardList = new ArrayList<>();
        cardList.add(tempCard);
        game.initCharacterCards(cardList);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("No active cards test")
    @Test
    void noActives() {
        // No cards present check
        cardList = new ArrayList<>();
        cardList.add(new Mushroom(""));
        game.initCharacterCards(cardList);
        card.activate(gameManager.getRules(), game);
        assertThrows(GameException.class, () -> {
            action.performMove(game, gameManager.getRules());
        });
    }

    @DisplayName("Mushroom color selection test")
    @Test
    void mushroomCard() {
        card.activate(gameManager.getRules(), game);
        try {
            action.performMove(game, gameManager.getRules());
            game.setGameState(action.nextState(game, gameManager.getRules()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        p1.getSchool().addStudentEntry(student);
        action = new MoveStudentFromEntryToHall(p1.getNickname(), student);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        game.setGameState(GameState.ACTION_MOVE_MOTHER);
        p1.setAndRemovePlayedCard(new AssistantCard("", 10));
        action = new MoveMotherNature(p1.getNickname(), 1);
        try {
            action.performMove(game, gameManager.getRules());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertNotEquals(p1.getNickname(), game.getIslandContainer().get(index).getOwner());
    }
}