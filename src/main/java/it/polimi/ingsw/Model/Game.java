package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Enumerations.Magician;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;

import java.util.*;

import static java.util.Optional.empty;

public class Game {

    private List<Player> players;
    private List<Player> playersActionPhase; // Ordered list of players for the action phase TODO ADD UML

    private List<Magician> magicians;
    private boolean expertMode;

    private IslandContainer islandContainer;
    private MotherNature motherNature;
    private List<Cloud> clouds;
    private EnumMap<Color, String> professors;
    private Bag bag;

    private List<CharacterCard> characterCards; //array, fixed size = 3
    private GameState gameState;
    private Player roundOwner;

    //private Comparator<Integer> influenceComparator = Comparator.comparing((i1,i2)->(i1.intValue()-i2));

    public Game(Bag bag) {
        this.bag = bag;
        players = new ArrayList<>();
        playersActionPhase = new ArrayList<>();
        magicians = new ArrayList<>();
        islandContainer = new IslandContainer();
        // motherNature = new MotherNature(); TODO
        clouds = new ArrayList<>();
        initProfessors();
        // characterCards = TODO
    }

    public void initProfessors() {
        professors = new EnumMap<Color, String>(Color.class);
        for (Color c : Color.values()) {
            // set 1 professor per color, initial state
            professors.put(c, null);
        }
    }
    public void initClouds(LinkedList<Cloud> clouds){
        //new object create with the same elements
        this.clouds.removeAll(clouds);
        this.clouds.addAll(clouds);
   }


    /**
     * @return list of Cards played until round owner
     */
    public List<AssistantCard> getPlayedCards() {
        ArrayList<AssistantCard> playedCards = new ArrayList<AssistantCard>();
        for (Player p : getOrderedPlanningPlayers()) {
            if (!p.equals(roundOwner)) {
                playedCards.add(p.getPlayedCard());
            } else {
                break;
            }
        }
        return playedCards;
    }

    /**
     * @return ordered players List for the next planning phase
     */
    public List<Player> getOrderedPlanningPlayers() {
        ArrayList<Player> orderedPlanningPLayers = new ArrayList<Player>();
        int playerIndex = players.indexOf(playersActionPhase.get(0));
        for (int i = 0; i < players.size(); i++) {
            orderedPlanningPLayers.add(players.get((playerIndex + i) % players.size()));
        }
        return orderedPlanningPLayers;
    }

    public  Optional<String> getNextPlayerActionPhase(){
      List<Player> players = playersActionPhase;
      int index = players.indexOf(roundOwner) + 1;
      if(index > players.size()){
          Optional<String> nothing = Optional.empty();
          return nothing;
      }
      return Optional.ofNullable(players.get(index).getNickname());

    }
    public  Optional<String> getNextPlayerPlanningPhase(){
        List<Player> players = getOrderedPlanningPlayers();
        int index = players.indexOf(roundOwner) + 1;
        if(index > players.size()){
            Optional<String> nothing = Optional.empty();
            return nothing;
        }
        return Optional.ofNullable(players.get(index).getNickname());
    }

    public List<Magician> getAvailableMagicians() { //choice for available magicians; main character
        return magicians;
    }

    public void removeMagician(Magician magician) { //delete the magicians from the possible choice
        magicians.remove(magician);

    }

    public boolean addPlayer(Player player) { //adding the player if the name isn't already taken
        if (!(isNicknameTaken(player.getNickname()))) {
            players.add(player);
            playersActionPhase = players;   // initialize players for action phase
            return true;
        }
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getRoundOwner() {
        return roundOwner;
    }

    public void setRoundOwner(Player roundOwner) {
        this.roundOwner = roundOwner;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayersActionPhase(List<Player> playersActionPhase) {
        this.playersActionPhase = playersActionPhase;
    }

    public Optional<Player> getPlayerByNickname(String name) { //getting the object player by nickname
        Player player = null;
        for (Player p : players) {
            if (p.getNickname().equals(name)) {
                player = p;
            }
        }
        return Optional.ofNullable(player);
    }

    private boolean isNicknameTaken(String name) { //check if the name is available
        for (Player p : players) {
            if (p.getNickname().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public EnumMap<Color, String> getProfessors() {
        return professors;
    }

    public void setProfessor(Color professor, String player) {
        this.professors.put(professor, player);
    }

    public Bag getBag() {
        return bag;
    }

    public int numPlayers() {
        return players.size();
    }

    public void initIslands(LinkedList<Island> islands) {
        this.islandContainer = new IslandContainer(islands);
    }

    public MotherNature getMotherNature() {
        return motherNature;
    }

    public void initMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
    }

    public IslandContainer getIslandContainer() {
        return islandContainer;
    }

    public void moveMotherNature(int deltaPositions) {
        int newPosition = motherNature.getPosition() + deltaPositions;
        newPosition = newPosition % islandContainer.size();
        motherNature.setIsland(newPosition);
    }
    public List<Cloud> getClouds(){
        return clouds;
    }

}
