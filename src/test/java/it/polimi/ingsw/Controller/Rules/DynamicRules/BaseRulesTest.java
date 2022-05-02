package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.BaseIsland;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class BaseRulesTest {
    private Player p1, p2;
    private Game game;
    private GameManager gameManager;
    private BaseRules baseRules;

    private void initGame() {
        gameManager = new GameManager(new Game());
        p1 = new Player("player1");
        p2 = new Player("player2");
        gameManager.addPlayer(p1);
        gameManager.addPlayer(p2);
        gameManager.initGame();
        game = gameManager.getGame();

        game.setRoundOwner(p1);

        baseRules = new BaseRules();
    }

    @Test
    void getProfessorInfluenceBaseCase() {
        initGame();
        // FIXME this test should be made in game
        for (String owner : game.getProfessors().values()) {
            assertNull(owner, "init of professors, all profs must not have an owner");
        }

        for (String owner : baseRules.getProfessorInfluence(game).values()) {
            assertNull(owner, "any player has a student in the all -> all professors must not have an owner");
        }


    }

    @Test
    void getProfessorInfluenceNormalUsage() {
        initGame();

        // add students to players
        p1.getSchool().addStudentsHall(Map.of(Color.BLUE, 4, Color.YELLOW, 2));
        p2.getSchool().addStudentsHall(Map.of(Color.BLUE, 3, Color.YELLOW, 1));

        EnumMap<Color, String> professorInfluence = baseRules.getProfessorInfluence(game);

        assertNull(professorInfluence.get(Color.GREEN), "no players have students with that color in the hall");
        assertNull(professorInfluence.get(Color.RED), "no players have students with that color in the hall");
        assertNull(professorInfluence.get(Color.PINK), "no players have students with that color in the hall");

        assertEquals(professorInfluence.get(Color.BLUE), p1.getNickname(), "p1 has more prof of that color in hall");
        assertEquals(professorInfluence.get(Color.YELLOW), p1.getNickname(), "p1 has more prof of that color in hall");
    }

    @Test
    void computeMotherMaxMoves() {
        initGame();

        AssistantCard card = new AssistantCard("sss", 8);
        assertEquals(baseRules.computeMotherMaxMoves(card), card.getMaxMoves());
    }

    @Test
    void computeIslandInfluenceNormalUsage() {
        initGame();

        Island island = new BaseIsland();
        Optional<String> player = baseRules.computeIslandInfluence(game, island);
        assertTrue(player.isEmpty(), "no students on that island -> no owners");

        // Add some students to the island
        island.addStudent(Color.BLUE);
        island.addStudent(Color.BLUE);
        island.addStudent(Color.PINK);

        // give p1 the blu prof
        p1.getSchool().addStudentHall(Color.BLUE);
        // give p2 the Pink prof
        p2.getSchool().addStudentHall(Color.PINK);
        game.setProfessors(baseRules.getProfessorInfluence(game));

        player = baseRules.computeIslandInfluence(game, island);
        assertTrue(player.isPresent(), "the influence on this island can be computed (there is a winner player)");
        assertEquals(player.get(), p1.getNickname(), "the winner should be p1 (2 students blue)");

        island.addStudent(Color.PINK);
        player = baseRules.computeIslandInfluence(game, island);
        assertTrue(player.isEmpty(), "Same number of students on island");

        // add a tower
        island.setOwner(p1.getNickname());
        player = baseRules.computeIslandInfluence(game, island);
        assertTrue(player.isPresent(), "the influence on this island can be computed (there is a winner player)");
        assertEquals(player.get(), p1.getNickname(), "the winner should be p1 (same students but a tower for p1)");
    }
}