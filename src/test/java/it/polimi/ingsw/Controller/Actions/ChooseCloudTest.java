package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChooseCloudTest {

    GameManager gameManager;
    Player p1, p2;
    Game gameInstance;

    private void initTest(){
        gameManager = new GameManager(new Game());
        p1 = new Player("Palkia");
        p2 = new Player("Kyogre");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        gameInstance = gameManager.getGame();
        gameInstance.setRoundOwner(p2);
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);

    }
    @Test
    void canPerformExt() {
        initTest();
        int choice = 1;

        Performable chooseCloud= new ChooseCloud(p2.getNickname(), choice);

        // base case
        assertTrue(chooseCloud.canPerformExt(gameInstance, gameManager.getRules()), "base case");

        //wrong game phase
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);
        assertFalse(chooseCloud.canPerformExt(gameInstance, gameManager.getRules()),"wrong game phase");

        // wrong player ( no player with that nickname )
        Performable chooseCloudWrongNick = new ChooseCloud("Scaccia", choice);
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        assertFalse(chooseCloudWrongNick.canPerformExt(gameInstance, gameManager.getRules()), "wrong player");

        // wrong player is not your turn
        gameInstance.setRoundOwner(p1);
        assertFalse(chooseCloud.canPerformExt(gameInstance, gameManager.getRules()), "not round-owner");
        gameInstance.setRoundOwner(p2);


        // The choice doesn't exist
        Performable wrongChoiceAction = new ChooseCloud("Kyogre", 6);
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        assertFalse(wrongChoiceAction.canPerformExt(gameInstance, gameManager.getRules()));

        // all Clouds are empty

        Performable action2 = new ChooseCloud("Kyogre", choice);
        gameInstance.pickCloud(choice);
        assertFalse(action2.canPerformExt(gameInstance, gameManager.getRules()));

    }

    @Test
    void performMove() {

        initTest();

        Random random = new Random();
        //random choice between 1 - maxClouds
        int choice = random.nextInt(gameInstance.numPlayers());

        Performable chooseCloud = new ChooseCloud(p2.getNickname(), choice);
        assertTrue(chooseCloud.canPerformExt(gameInstance, gameManager.getRules()));

        // previous state
        int studentsPerTurn = Rules.getStudentsPerTurn(gameInstance.numPlayers());
        Map<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry());
        Cloud selectedCloud = gameInstance.getClouds().get(choice);

        //perform move
        chooseCloud.performMove(gameInstance, gameManager.getRules());

        Map<Color, Integer> postEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry());


        int previousPlayerStudents = prevEntry.values().stream().reduce(0,Integer::sum);
        int postPlayerStudents = postEntry.values().stream().reduce(0,Integer::sum);

        assertEquals(previousPlayerStudents + studentsPerTurn, postPlayerStudents, "the right number of student has been added");
        //assertTrue(selectedCloud.isEmpty(), "after having picked students the cloud must be empty"); // Not true if the current player is the last one ( refill )

    }


}