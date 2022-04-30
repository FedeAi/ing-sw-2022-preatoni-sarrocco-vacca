package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.KnightCharacter;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class DeactivateCardTest {
    // TODO MAYBE THIS CAN BE DONE BETTER BY CALL THE RESPECTIVE UNDERLYING ACTION: Performable_ActivateCard
    // Also we have the ActivateCardTest so I don't know if it's really needed
    private KnightCharacter card;
    private Game game;
    private GameManager gameManager;
    private Player p1, p2, p3;
    DeactivateCard action;
    LinkedList<CharacterCard> characterList;

    private void init() {
        card = new KnightCharacter("");
        gameManager = new GameManager(new Game());
        p1 = new Player("Ale");
        p2 = new Player("Fede");
        p3 = new Player("Davide");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.addPlayer(p3);
        gameManager.initGame();
        game = gameManager.getGame();
        game.setGameState(GameState.ACTION_MOVE_STUDENTS);
        game.setRoundOwner(p1);
    }

    @Test
    void canPerformExt() {
        init();

        // First we check the underlying Performable call
        String wrongNickname = "Franco";
        action = new DeactivateCard(wrongNickname);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Then we try to perform the action with no characterCards present
        action = new DeactivateCard(p1.getNickname());
        characterList = new LinkedList<>();
        game.initCharacterCards(characterList);
        assertFalse(action.canPerformExt(game, gameManager.getRules()));
        // Now we retry but with actual cards present
        card.activate(gameManager.getRules(), game);
        characterList.add(card);
        assertTrue(action.canPerformExt(game, gameManager.getRules()));
    }

    @Test
    void performMove() {
        init();
        card.activate(gameManager.getRules(), game);
        characterList = new LinkedList<>();
        characterList.add(card);
        game.initCharacterCards(characterList);
        action = new DeactivateCard(p1.getNickname());
        action.performMove(game, gameManager.getRules());
        assertFalse(game.getCharacterCards().get(0).isActive());
    }
}