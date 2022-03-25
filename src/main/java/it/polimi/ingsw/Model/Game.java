package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.ArrayList;
import java.util.List;

public class Game {

    static final int maxPlayers = 4;
    static final int maxIslands = 12;

    private static Game instance;

    private List<Player> players;
    private List<Player> playersActionPhase; // Ordered list of players for the action phase TODO ADD UML

    private List<Magician> magicians;
    private boolean expertMode;

    private MotherNature motherNature;
    private List<Island> islands;
    private List<Cloud> clouds;

    private List<CharacterCard> characterCards; //array, fixed size = 3
    private GameState gameState;
    private Player roundOwner; // TODO add uml


    /**
     * The singleton instance of the game returns, if it has not been created it allocates it as well
     *
     * @return the singleton instance
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     *
     * @return list of Cards played until round owner
     */
    public List<AssistantCard> getPlayedCards(){
        ArrayList<AssistantCard> playedCards = new ArrayList<AssistantCard>();
        for(Player p : getOrderedPlanningPlayers()){
            if(!p.equals(roundOwner)){
                playedCards.add(p.getPlayedCard());
            }else{
                break;
            }

        }
        return playedCards;
    }

    /**
     *
     * @return ordered players List for the next planning phase
     */
    public List<Player> getOrderedPlanningPlayers(){
        ArrayList<Player> orderedPlanningPLayers = new ArrayList<Player>();
        int playerIndex = players.indexOf(playersActionPhase.get(0));
        for(int i = 0; i < players.size(); i++){
            orderedPlanningPLayers.add(players.get((playerIndex + i) % players.size()));
        }
        return orderedPlanningPLayers;
    }

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

    public Player getRoundOwner(){
        return roundOwner;
    }

    public void setRoundOwner(Player roundOwner){
        this.roundOwner = roundOwner;
    }

    public GameState getGameState() {
        return gameState;
    }
    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }

    public void setPlayersActionPhase(List<Player> playersActionPhase) {
        this.playersActionPhase = playersActionPhase;
    }
}
