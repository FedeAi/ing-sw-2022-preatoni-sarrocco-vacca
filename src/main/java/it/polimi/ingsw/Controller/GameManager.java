package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.GameStates;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

public class GameManager {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private final Game gameInstance = null;
    private Game game;
    private RoundManager roundManager;


    public GameManager() {

        this.gameInstance.setGameStatus(GameStates.GAME_ROOM);
        this.gameInstance = Game.getInstance();
        this.roundManager = new RoundManager(this);


    }


    public void addPlayer(Player player){
        game.addPlayer(player);
    }

    public Game getGameInstance() {
        return this.gameInstance;
    }
    public RoundManager getRoundManager() {
        return this.roundManager;
    }





}
