package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;

import java.util.List;
import java.util.Map;

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


    public Game getGameIstance(){ return gameInstance; }

    public void addPlayer(Player player){
        this.gameInstance.addPlayer(player);
    }

    public void initGame(){
        // init Model
        initSchools();
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



}
