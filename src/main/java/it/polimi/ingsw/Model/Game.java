package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Enumerations.Magician;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;

import java.util.*;

public class Game {

    private final List<Player> players;
    private final List<Magician> magicians;
    private final List<Cloud> clouds;
    private final Bag bag;
    private int balance;
    private List<Player> playersActionPhase; // Ordered list of players for the action phase TODO ADD UML
    private boolean expertMode;
    private IslandContainer islandContainer;
    private MotherNature motherNature;
    private EnumMap<Color, String> professors;
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
        characterCards = new ArrayList<>();
        initProfessors();
        expertMode = false;
        // characterCards = TODO
    }

    public void initProfessors() {
        professors = new EnumMap<Color, String>(Color.class);
        for (Color c : Color.values()) {
            // set 1 professor per color, initial state
            professors.put(c, null);
        }
    }

    public void initClouds(LinkedList<Cloud> clouds) {
        //new object create with the same elements
        this.clouds.removeAll(clouds);
        this.clouds.addAll(clouds);
    }

    public void initMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
    }

    public void initIslands(LinkedList<Island> islands) {
        this.islandContainer = new IslandContainer(islands);
    }

    public void initCharacterCards(List<CharacterCard> cards) {
        this.characterCards = cards;
    }

    public void initBalance(int balance){
        this.balance = balance;
    }

    public void incrementPlayerBalance(String nickname){
        getPlayerByNickname(nickname).ifPresent(player -> {
            if(balance>0){
                player.addCoin();
                balance--;
            }
        });
    }
    public void decrementBalance(){
        balance--;
    }

    public void incrementBalance(int value){
        balance+= value;
    }

    /**
     * check if the name is available
     *
     * @param name
     * @return
     */
    private boolean isNicknameTaken(String name) {
        return getPlayerByNickname(name).isPresent();
    }

    public void removeMagician(Magician magician) { //delete the magicians from the possible choice
        magicians.remove(magician);

    }

    public boolean isExpertMode() {
        return expertMode;
    }

    // This will likely need a MUCH better solution
    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    public boolean addPlayer(Player player) { //adding the player if the name isn't already taken
        if (!(isNicknameTaken(player.getNickname()))) {
            players.add(player);
            playersActionPhase = players;   // initialize players for action phase
            return true;
        }
        return false;
    }

    public void moveMotherNature(int deltaPositions) {
        int newPosition = (motherNature.getPosition() + deltaPositions) % islandContainer.size();
        motherNature.setIsland(newPosition);
    }

    public void setPlayersActionPhase(List<Player> playersActionPhase) {
        this.playersActionPhase = playersActionPhase;
    }

    public void setProfessor(Color professor, String player) {
        this.professors.put(professor, player);
    }

    public void setProfessors(EnumMap<Color, String> professors) {
        this.professors = professors;
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

    /**
     * it computes the next player in action phase, if it's end of turn it returns an Empty Optional
     * Warning this method works only if the game is in action phase
     *
     * @return
     */
    public Optional<String> getNextPlayerActionPhase() {
        List<Player> players = playersActionPhase;
        int index = players.indexOf(roundOwner) + 1;
        if (index >= players.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(players.get(index).getNickname());
    }

    /**
     * it computes the next player in planning phase, if it's end of turn it returns an Empty Optional
     * Warning this method works only if the game is in planning phase
     *
     * @return
     */
    public Optional<String> getNextPlayerPlanningPhase() {
        List<Player> players = getOrderedPlanningPlayers();
        int index = players.indexOf(roundOwner) + 1;
        if (index > players.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(players.get(index).getNickname());
    }

    public List<Magician> getAvailableMagicians() { //choice for available magicians; main character
        return magicians;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getRoundOwner() {
        return roundOwner;
    }

    public void setRoundOwner(Player roundOwner) {
        this.roundOwner = roundOwner;
    }

    public EnumMap<Color, String> getProfessors() {
        return professors;
    }

    public MotherNature getMotherNature() {
        return motherNature;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Bag getBag() {
        return bag;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public IslandContainer getIslandContainer() {
        return islandContainer;
    }

    public Optional<CharacterCard> getActiveCharacter() {
        return characterCards.stream().filter(c -> c.isActive()).findFirst();
    }

    public int numPlayers() {
        return players.size();
    }


    /**
     * It return the Player, if there is no player with that nickname the return value is Optional.Empty
     *
     * @param name
     * @return
     */
    public Optional<Player> getPlayerByNickname(String name) { //getting the object player by nickname
        return players.stream().filter(player -> player.getNickname().equals(name)).findFirst();
    }


}
