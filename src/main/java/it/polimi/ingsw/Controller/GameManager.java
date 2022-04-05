package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameManager {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private final Game gameInstance;
    //private RoundManager roundManager;

    public GameManager() {

        this.gameInstance = new Game(new Bag(22));
        this.gameInstance.setGameState(GameState.GAME_ROOM);
        //this.roundManager = new RoundManager(this);
    }


    public Game getGameInstance(){ return gameInstance; }

    public void addPlayer(Player player){
        this.gameInstance.addPlayer(player);
    }

    public void initGame(){
        // init Model
        initSchools();
        initMotherNature();
        initIslands();
    }


    // TODO this can be moved inside Game?
    private void initSchools(){
        List<Player> players = gameInstance.getPlayers();
        for(Player p : players){
            // create and fill the school
            School school = new School(players.indexOf(p), players.size());
            Map<Color,Integer> students = gameInstance.getBag().extract(Rules.getEntrySize(players.size()));
            school.initEntry(students);

            p.setSchool(school);
        }
    }

    private void initIslands() {
        int motherNaturePosition = gameInstance.getMotherNature().getPosition();
        int opposite = (motherNaturePosition + 6) % 12;
        LinkedList<Island> islands = new LinkedList<>();
        MotherNature motherNature;

        for(int i = 0; i < Rules.maxIslands; i++) {
            Island island = new Island();
            if(i != opposite || i != motherNaturePosition) {
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
}
