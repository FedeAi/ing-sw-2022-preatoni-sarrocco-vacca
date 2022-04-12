package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChooseCloudTest {

    @Test
    void canPerformExt() {

        GameManager gameManager = new GameManager();
        Player p1 = new Player("Palkia");
        Player p2 = new Player("Kyogre");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p2);


        int choice = 1; // get right choice
        int choice2 = 6;


        Performable choiceCloudRIGHT = new ChooseCloud("Kyogre", choice);
        Performable choiceCloudWRONG = new ChooseCloud("Scaccia", choice);


        // base case
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        assertTrue(choiceCloudRIGHT.canPerformExt(gameInstance, ));

        //wrong game phase
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);
        assertFalse(choiceCloudWRONG.canPerformExt(gameInstance, ));

        // wrong player ( no player with that nickname )
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        assertFalse(choiceCloudWRONG.canPerformExt(gameInstance, ));

        // wrong player is not your turn
        gameInstance.setRoundOwner(p1);
        assertFalse(choiceCloudRIGHT.canPerformExt(gameInstance, ));
        gameInstance.setRoundOwner(p2);


        // The choice doesn't exist
        Performable action = new ChooseCloud("Kyogre",choice2);
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        assertFalse(action.canPerformExt(gameInstance, ));

        // all Clouds are empty

        Performable action2 = new ChooseCloud("Kyogre",choice);
        gameInstance.getClouds().get(choice).pickStudents();
        assertFalse(action2.canPerformExt(gameInstance, ));

    }

    @Test
    void performMove() {

        GameManager gameManager = new GameManager();
        Player p1 = new Player("Palkia");
        Player p2 = new Player("Kyogre");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setGameState(GameState.ACTION_CHOOSE_CLOUD);
        gameInstance.setRoundOwner(p2);

        Random random = new Random();
        int choice = random.nextInt(Rules.getStudentsPerTurn(gameInstance.numPlayers())); //random choice between 1 - maxClouds

        Performable ChooseClouds = new ChooseCloud("Kyogre", choice);
        assertTrue(ChooseClouds.canPerformExt(gameInstance, ));

        // previous state
        int weight = Rules.getStudentsPerTurn(gameInstance.numPlayers());
        Map<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry()); //empty?
        //perform move
        
        ChooseClouds.performMove(gameInstance, gameManager.getRules());

        Map<Color, Integer> postEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry());

        int count = 0;
        for(Color c: Color.values()){
            count = count + prevEntry.get(c);
        }

        int postCounter = 0;
        for(Color c: Color.values()){
            postCounter = postCounter + postEntry.get(c);
        }

        assertTrue(count + weight == postCounter);

    }


}