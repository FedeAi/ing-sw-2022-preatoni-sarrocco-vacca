package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

class BaseRulesTest {
    private Player p1,p2;
    private Game game;
    private GameManager gameManager;
    private BaseRules baseRules;

    private void initGame(){
        gameManager = new GameManager();
        p1 = new Player("player1");
        p2 = new Player("player2");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        game = gameManager.getGameInstance();

        game.setRoundOwner(p1);

        baseRules = new BaseRules();
    }
    @Test
    void checkProfessorInfluenceBaseCase() {
        initGame();
        //Island i = game.getIslandContainer().get(5);
        // add students to players
        //p1.getSchool().addStudentsEntry(Map.of(Color.BLUE,4,Color.YELLOW,2));
        //p2.getSchool().addStudentsEntry(Map.of(Color.BLUE,3,Color.YELLOW,1));
        
        
        baseRules.getProfessorInfluence(game);


    }

    @Test
    void computeMotherMaxMoves() {
    }

    @Test
    void computeIslandInfluence() {
    }
}