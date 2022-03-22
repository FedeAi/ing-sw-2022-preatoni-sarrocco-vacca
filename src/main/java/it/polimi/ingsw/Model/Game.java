package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.ArrayList;
import java.util.List;

public class Game {

    static final int maxPlayer = 4;
    static final int maxIsland = 12;

    //private Table table; //table of the game
    private List<Player> players;
    private List<Magician> magicians;
    private boolean expertMode;

    private MotherNature motherNature;
    private ArrayList<Island> islands;
    private ArrayList<CharacterCard> characterCards; //array, fixed size = 3


//    public  void startGame(){ //method for starting the game
//
//    }
//    public void endGame(){ //if win conditions then endgame()
//
//    }

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
    public List<Player> getPlayers(){
        return players;
    }

    public MotherNature getMotherNature() {
        return motherNature;
    }

    public void setMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public void setIslands(ArrayList<Island> islands) {
        this.islands = islands;
    }

    public ArrayList<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public void setCharacterCards(ArrayList<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

}
