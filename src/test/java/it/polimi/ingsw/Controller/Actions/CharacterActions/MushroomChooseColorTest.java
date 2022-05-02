package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MushroomCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
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
    CharacterCard mushroom;
    List<CharacterCard> cardList;
    Color studentColor;

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
        mushroom = new MushroomCharacter("");
        cardList = new ArrayList<>();
        cardList.add(mushroom);
        game.initCharacterCards(cardList);
    }
    @Test
    @DisplayName("Wrong nickname")
    void superTest() {
        String wrongNickname = "Kraken";
        action = new MushroomChooseColor(wrongNickname, studentColor);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Wrong state")
    void wrongState() {
        // We aren't set to the correct state
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        action = new MushroomChooseColor(p1.getNickname(), studentColor);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    @DisplayName("Zero cards are active")
    void zeroActives() {
        game.setGameState(GameState.MUSHROOM_CHOOSE_COLOR);
        action = new MushroomChooseColor(p1.getNickname(), studentColor);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
    }

    // TODO FINISH THIS
}