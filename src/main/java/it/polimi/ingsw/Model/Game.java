package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.GameStates;
import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.ArrayList;
import java.util.List;

public class Game {

    static final int maxPlayers = 4;
    static final int maxIslands = 12;

    //private Table table; //table of the game
    private List<Player> players;
    private List<Magician> magicians;
    private boolean expertMode;

    private MotherNature motherNature;
    private ArrayList<Island> islands;
    private ArrayList<CharacterCard> characterCards; //array, fixed size = 3
    private GameStates gameStatus;



    public String getPlayerNickname(Player player){

            return player.getNickname();

    }
    public List<String> getAvailableMagicians() { //choice for available magicians; main character
        assert false;
        return null;
    }
    public List<String> isNicknameTaken() { //Control the choice of nickname must be unique
        assert false;
        return null;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public GameStates getGameStatus() {
        return gameStatus;
    }
}
