package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.BaseIsland;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class BaseRulesTest {
    Player p1, p2;
    Game game;
    GameManager gameManager;
    BaseRules baseRules;

    @BeforeEach
    void init() {
        gameManager = new GameManager(new Game(), new GameHandler(new Server()));
        game = gameManager.getGame();
        game.createPlayer(0, "Ale");
        game.createPlayer(1, "Fede");
        p1 = game.getPlayers().get(0);
        p2 = game.getPlayers().get(1);
        gameManager.initGame();
        game.setRoundOwner(p1);
        baseRules = new BaseRules();
    }

    @Test
    void getProfessorInfluenceBaseCase() {
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
        AssistantCard card = new AssistantCard("sss", 8);
        assertEquals(baseRules.computeMotherMaxMoves(card), card.getMaxMoves());
    }

    @Test
    void computeIslandInfluenceNormalUsage() {
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