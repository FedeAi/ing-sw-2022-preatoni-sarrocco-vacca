package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.MotherNature;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameManager {
    private Game game;

    public void addPlayer(Player player){
        game.addPlayer(player);
    }

    public Game getGameIstance(){
        return game;    // todo make sigleton GP
    }

}
