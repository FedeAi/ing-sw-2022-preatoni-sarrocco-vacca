package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {

    static final int maxPlayer = 4;
    static final int maxIsland = 12;

    private Table table; //table of the game
    private ArrayList<Player> players;
    private ArrayList<Magician> magicians;
    private boolean expertMode;


    public  void startGame(){ //method for starting the game

    }
    public void endGame(){ //if win conditions then endgame()

    }
    public String getPlayerNickname(Player player){

            return player.getNickname();

    }
    public ArrayList<String> getAvailableMagicians() { //choice for available magicians; main character

        assert false;
        return null;

    }
    public ArrayList<String> isNicknameTaken() { //Control the choice of nickname must be unique

        assert false;
        return null;

    }

}
