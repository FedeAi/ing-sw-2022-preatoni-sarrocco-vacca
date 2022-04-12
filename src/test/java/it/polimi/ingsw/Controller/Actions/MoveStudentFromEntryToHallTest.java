package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentFromEntryToHallTest {

    @Test
    void canPerformExt() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("ManovellismoOrdinario");
        Player p2 = new Player("Biella");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setRoundOwner(p2);

        Color student = getStudentFromEntry(p2); // get a student of the player Entry
        Performable moveStudentsToHallAction = new MoveStudentFromEntryToHall("Biella", student);
        Performable moveStudentsToHallActionWrongPlayer = new MoveStudentFromEntryToHall("Gianfranco", student);

        // base case
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertTrue(moveStudentsToHallAction.canPerformExt(gameInstance));

        //wrong game phase
        gameInstance.setGameState(GameState.PLANNING_CHOOSE_CARD);
        assertFalse(moveStudentsToHallAction.canPerformExt(gameInstance));

        // wrong player ( no player with thath nick name )
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        assertFalse(moveStudentsToHallActionWrongPlayer.canPerformExt(gameInstance));
        // wrong player is not your turn
        gameInstance.setRoundOwner(p1);
        assertFalse(moveStudentsToHallAction.canPerformExt(gameInstance));
        gameInstance.setRoundOwner(p2);


        // The player has already moved all possible students
        Performable action = new MoveStudentFromEntryToHall("Biella", student);
        for(int i =0; i< Rules.getStudentsPerTurn(gameInstance.numPlayers()); i++){
            Color pickedStudent = getStudentFromEntry(p2);
            action = new MoveStudentFromEntryToHall("Biella", pickedStudent);
            assertTrue(action.canPerformExt(gameInstance));

            p2.getSchool().moveStudentFromEntryToHall(pickedStudent);
        }
        assertFalse(action.canPerformExt(gameInstance));
    }

    @Test
    void performMove() {
        GameManager gameManager = new GameManager();
        Player p1 = new Player("ManovellismoOrdinario");
        Player p2 = new Player("Biella");

        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        Game gameInstance = gameManager.getGameInstance();
        gameInstance.setGameState(GameState.ACTION_MOVE_STUDENTS);
        gameInstance.setRoundOwner(p2);

        Color student = getStudentFromEntry(p2); // get a student of the player Entry
        Performable moveStudentsToHallAction = new MoveStudentFromEntryToHall("Biella", student);
        assertTrue(moveStudentsToHallAction.canPerformExt(gameInstance));

        // previous state
        Map<Color, Integer> prevEntry = new EnumMap<Color, Integer>(p2.getSchool().getStudentsEntry());
        Map<Color, Integer> prevHall = new EnumMap<Color, Integer>(p2.getSchool().getStudentsHall());
        // perform move
        moveStudentsToHallAction.performMove(gameInstance, gameManager.getRules());

        // checks
        // entry has not been modified except...
        Player p = gameInstance.getPlayerByNickname("Biella").get();
        for(Map.Entry<Color,Integer> entry : prevEntry.entrySet()){
            int students = p.getSchool().getStudentsEntry().get(entry.getKey());
            if(entry.getKey() != student){
                assertEquals(entry.getValue(), students);
            }
            else{
                assertEquals(entry.getValue(), students+1);
            }
        }
        // hall has not been modified except...
        for(Map.Entry<Color,Integer> entry : prevHall.entrySet()){
            int students = p.getSchool().getStudentsHall().get(entry.getKey());
            if(entry.getKey() != student){
                assertEquals(entry.getValue(), students);
            }
            else{
                assertEquals(entry.getValue(), students-1);
            }
        }



    }

    private Color getStudentFromEntry(Player p){
        return p.getSchool().getStudentsEntry().entrySet().stream().filter((s)->s.getValue()>0).findFirst().get().getKey();
    }
}