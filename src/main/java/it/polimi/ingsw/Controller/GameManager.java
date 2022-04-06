package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Rules.DynamicRules.DynamicRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Enumerations.TowerColor;

import java.util.*;

public class GameManager {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private final Game gameInstance;
    private final Rules rules;
    //private RoundManager roundManager;

    public GameManager() {

        this.gameInstance = new Game(new Bag(Rules.initialBagSize));
        this.gameInstance.setGameState(GameState.GAME_ROOM);
        rules = new Rules();
        //this.roundManager = new RoundManager(this);
    }


    public Game getGameInstance() {
        return gameInstance;
    }

    public Rules getRules(){
        return rules;
    }

    public void addPlayer(Player player) {
        this.gameInstance.addPlayer(player);
    }

    public void initGame() {
        // init Model
        initMotherNature();
        initIslands();
        fillBag();
        initSchools();
    }


    // TODO this can be moved inside Game?
    private void initSchools() {
        List<Player> players = gameInstance.getPlayers();
        for (Player p : players) {
            // create and fill the school
            School school = new School(players.indexOf(p), players.size());
            Map<Color, Integer> students = gameInstance.getBag().extract(Rules.getEntrySize(players.size()));
            school.initEntry(students);

            p.setSchool(school);
        }
    }

    private void initIslands() {
        int motherNaturePosition = gameInstance.getMotherNature().getPosition();
        int opposite = (motherNaturePosition + 6) % 12;
        LinkedList<Island> islands = new LinkedList<>();
        MotherNature motherNature;

        for (int i = 0; i < Rules.maxIslands; i++) {
            Island island = new Island();
            if (i != opposite && i != motherNaturePosition) {
                island.addStudent(gameInstance.getBag().extractOne());
            }
            islands.add(island);
        }
    }

    private void initMotherNature() {
        Random rand = new Random();
        int motherNaturePosition = rand.nextInt(1, Rules.maxIslands);
        MotherNature motherNature = new MotherNature(motherNaturePosition);
        gameInstance.initMotherNature(motherNature);
    }

    private void fillBag() {
        gameInstance.getBag().extendBag(Rules.bagSize - Rules.initialBagSize);
    }

    private void initTowers() {
        List<Player> players = gameInstance.getPlayers();
        List<TowerColor> towerColors = null; // FIXME Davide
        for (TowerColor colors : towerColors) { //for each color, based on rules create the right number of towerscolors
            for (int j = 0; j < Rules.getTowersPerPlayer(players.size()) && colors.ordinal() <= players.size(); j++) { //check the right number and right number of players
                towerColors.add(colors);
                // gameInstance.init
            }
        }
    }
}

