package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.GameStates;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

public class GameManager {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private final Game gameInstance;
    //private RoundManager roundManager;

    public GameManager() {
        this.gameInstance = Game.getInstance();
        this.gameInstance.setGameState(GameStates.GAME_ROOM);
        //this.roundManager = new RoundManager(this);
    }



    public void addPlayer(Player player){
        this.gameInstance.addPlayer(player);
    }

    public RoundManager getRoundManager() {//TODO controllare
        return this.roundManager;
    }





}
