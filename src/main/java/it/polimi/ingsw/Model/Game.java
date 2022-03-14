package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {

    int maxplayer = 4;
    int maxisland = 12;
    Table table; //table of the game
    ArrayList<Player> players;
    ArrayList<Magician> magicians;
    Boolean expertmode;


    public  void startgame(){ //method for starting the game

    }
    public void endgame(){ //if win conditions then endgame()

    }
    public String getPlayerNickname(Player player){

            return this.getPlayerNickname();

    }
    public ArrayList<String> getAvaliableMagicinas() {

        assert false;
        return 0;

    }
    public ArrayList<String> isNicknameTaken() {

        assert false;
        return 0;

    }

}
