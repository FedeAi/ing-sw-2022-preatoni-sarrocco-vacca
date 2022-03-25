package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.GameStates;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

public class GameManager {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private Game game;
    private final Game gameInstance = null;
    private RoundManager roundManager;

    public GameManager(Game game) {
        this.game = game;
        this.gameInstance.setGameStatus(GameStates.GAME_ROOM);

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
