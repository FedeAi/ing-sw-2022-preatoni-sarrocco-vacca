package it.polimi.ingsw.Model;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Server.VirtualClient;
import it.polimi.ingsw.listeners.*;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    public static final String MOVE_MOTHER_LISTENER = "moveMotherListener";
    public static final String ISLANDS_LISTENER = "islandsListener";
    public static final String PLAYED_CARD_LISTENER = "playedCardListener";
    public static final String CLOUDS_LISTENER = "cloudsListener";
    public static final String PROFS_LISTENER = "profsListener";
    public static final String ROUND_OWNER_LISTENER = "roundOwnerListener";
    public static final String MAGICIANS_LISTENER = "magiciansListener";
    public static final String GAME_STATE_LISTENER = "gameStateListener";

    private final List<Player> players;
    private final List<Magician> availableMagicians;
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

    protected final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    //private Comparator<Integer> influenceComparator = Comparator.comparing((i1,i2)->(i1.intValue()-i2));

    public Game() {
        this.bag = new Bag(Constants.INITIAL_BAG_SIZE);
        players = new ArrayList<>();
        playersActionPhase = new ArrayList<>();
        availableMagicians = new ArrayList<>(Arrays.stream(Magician.values()).toList());
        listeners.firePropertyChange(MAGICIANS_LISTENER, null, availableMagicians);

        islandContainer = new IslandContainer();
        clouds = new ArrayList<>();
        characterCards = new ArrayList<>();
        initProfessors();
        expertMode = false;

    }

    /**
     * Method createListeners creates the Map of listeners.
     * @param client virtualClient - the VirtualClient on the server.
     */
    public void createListeners(VirtualClient client){
        listeners.addPropertyChangeListener(MOVE_MOTHER_LISTENER, new MoveMotherListener(client));
        listeners.addPropertyChangeListener(ISLANDS_LISTENER, new IslandsListener(client));
        listeners.addPropertyChangeListener(PLAYED_CARD_LISTENER, new PlayedCardListener(client));
        listeners.addPropertyChangeListener(CLOUDS_LISTENER, new CloudsListener(client));
        listeners.addPropertyChangeListener(PROFS_LISTENER, new ProfsListener(client));
        listeners.addPropertyChangeListener(ROUND_OWNER_LISTENER, new RoundOwnerListener(client));
        listeners.addPropertyChangeListener(MAGICIANS_LISTENER, new MagicianListener(client));
        listeners.addPropertyChangeListener(GAME_STATE_LISTENER, new GameStateListener(client));

        // Player listeners
        Optional<Player> player = getPlayerByNickname(client.getNickname());
        player.ifPresent(value -> value.createListeners(client));
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
        listeners.firePropertyChange(MOVE_MOTHER_LISTENER, null, motherNature.getPosition());
    }

    public void initIslands(LinkedList<Island> islands) {
        this.islandContainer = new IslandContainer(islands);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    public void initCharacterCards(List<CharacterCard> cards) {
        this.characterCards = cards;
    }

    public void initBalance(int balance) {
        this.balance = balance;
    }

    /**
     * increment player balance and decrease game balance
     *
     * @param nickname
     */
    public void incrementPlayerBalance(String nickname) {
        getPlayerByNickname(nickname).ifPresent(player -> {
            if (balance > 0) {
                player.addCoin();
                balance--;
            }
        });
    }

    public void decrementBalance() {
        balance--;
    }

    public void incrementBalance(int value) {
        balance += value;
    }

    public int getBalance() {
        return balance;
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


    // FIXME GIGANTESCO
    public void createPlayer(int playerID, String nickname) {
        Player p = new Player(playerID, nickname);
        players.add(p);
    }

    /**
     * Method chooseMagician() allows players to select an available magician to play with.
     *
     * @param p - the player
     * @param magician - the chosen magician
     */
    public void chooseMagician(Player p, Magician magician) {
        p.setMagician(magician);
        List<Magician> oldMagicians = new ArrayList<>(availableMagicians);
        availableMagicians.remove(magician);
        playersActionPhase = players;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    // This will likely need a MUCH better solution
    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    /**
     * @deprecated
     * @param player
     * @return
     */
    public boolean addPlayer(Player player) { //adding the player if the name isn't already taken
        if (!(isNicknameTaken(player.getNickname()))) {
            players.add(player);
            playersActionPhase = players;   // initialize players for action phase
            return true;
        }
        return false;
    }

    public void moveMotherNature(int deltaPositions) {
        int oldPosition = motherNature.getPosition();
        int newPosition = (oldPosition + deltaPositions + islandContainer.size()) % islandContainer.size();
        motherNature.setIsland(newPosition);
        listeners.firePropertyChange(MOVE_MOTHER_LISTENER, oldPosition, newPosition);
    }

    public void setPlayersActionPhase(List<Player> playersActionPhase) {
        this.playersActionPhase = playersActionPhase;
    }

    /**
     * @deprecated
     */
    public void setProfessor(Color professor, String player) {
        this.professors.put(professor, player);
    }

    public void setProfessors(EnumMap<Color, String> professors) {
        EnumMap<Color, String> oldProfs = new EnumMap<>(professors);
        this.professors = professors;
        listeners.firePropertyChange(PROFS_LISTENER, oldProfs, professors);
    }

    /**
     * @return list of Cards played until round owner
     * TODO maybe is broken test FIXME
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
        return availableMagicians;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public List<Player> getActivePlayers() {
        return players.stream().filter(Player::isConnected).collect(Collectors.toList());
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        listeners.firePropertyChange(GAME_STATE_LISTENER, this.gameState , gameState);
        this.gameState = gameState;
    }

    public Player getRoundOwner() {
        return roundOwner;
    }

    public void setRoundOwner(Player roundOwner) {
        if(this.roundOwner != roundOwner){
            listeners.firePropertyChange(ROUND_OWNER_LISTENER, this.roundOwner, roundOwner.getNickname());
            this.roundOwner = roundOwner;

        }

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
        return characterCards.stream().filter(CharacterCard::isActive).findFirst();
    }

    public int numPlayers() {
        return players.size();
    }

    public void refillClouds() {
        for (Cloud c : clouds) {
            c.pickStudents(); //sure of to empty the cloud
            c.addStudents(bag.extract(Rules.getStudentsPerTurn(numPlayers()))); //refill the same cloud
        }
        listeners.firePropertyChange(CLOUDS_LISTENER, null, clouds);
    }

    public Map<Color, Integer> pickCloud(int cloudIndex) throws IndexOutOfBoundsException{
        Cloud cloud = clouds.get(cloudIndex);
        Map<Color, Integer> students = cloud.pickStudents();
        listeners.firePropertyChange(CLOUDS_LISTENER, null, clouds);
        return students;
    }

    public void addIslandStudent(int islandIndex, Color student){
        islandContainer.addIslandStudent(islandIndex, student);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);  // TODO old value?
    }

    public void playCard(Player player, AssistantCard playedCard) {
        player.setAndRemovePlayedCard(playedCard);
        listeners.firePropertyChange(PLAYED_CARD_LISTENER, null, playedCard);
    }

    /**
     * It returns the Player, if there is no player with that nickname the return value is Optional.Empty
     *
     * @param name
     * @return
     */
    public Optional<Player> getPlayerByNickname(String name) { //getting the object player by nickname
        return players.stream().filter(player -> player.getNickname().equals(name)).findFirst();
    }

    // TODO make it like getPlayerByNickname
    public Player getPlayerByID(int id) {
        for (Player player : players) {
            if (player.getID() == id) {
                return player;
            }
        }
        return null;
    }

    public void removeMagician(int chooseRemove){
        ArrayList<Magician> previousList = new ArrayList<Magician>(availableMagicians);
        availableMagicians.remove(chooseRemove);
        listeners.firePropertyChange(MAGICIANS_LISTENER, previousList, availableMagicians);
    }
}
