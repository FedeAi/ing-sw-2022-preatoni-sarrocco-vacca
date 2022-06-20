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
import it.polimi.ingsw.Server.Answer.modelUpdate.PlayersStatusMessage;
import it.polimi.ingsw.Server.VirtualClient;
import it.polimi.ingsw.listeners.*;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    public static final String MOVE_MOTHER_LISTENER = "moveMotherListener";
    public static final String ISLANDS_LISTENER = "islandsListener";
    public static final String CLOUDS_LISTENER = "cloudsListener";
    public static final String PROFS_LISTENER = "profsListener";
    public static final String ROUND_OWNER_LISTENER = "roundOwnerListener";
    public static final String MAGICIANS_LISTENER = "magiciansListener";
    public static final String GAME_STATE_LISTENER = "gameStateListener";
    private static final String MODE_LISTENER = "modeListener";
    private static final String CHARACTERS_LISTENER = "charactersListener";
    private static final String PLAYERS_LISTENER = "activePlayersListener";

    private final List<Player> players;
    private final Map<Magician, String> mapMagicianToPlayer;
    private final List<Cloud> clouds;
    private final Bag bag;
    private int balance;
    private List<Player> playersActionPhase; // Ordered list of players for the action phase
    private List<Player> playersPlanningPhase; // Ordered list of players for the planning phase
    private List<String> waitingPlayersReconnected; //List of players to be re-inserted in the game
    private boolean expertMode;
    private IslandContainer islandContainer;
    private MotherNature motherNature;
    private EnumMap<Color, String> professors;
    private List<CharacterCard> characterCards; //array, fixed size = 3
    private GameState gameState;
    private Player roundOwner;

    protected final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private final HashMap<VirtualClient, List<AbsListener>> clientMapLister = new HashMap<VirtualClient, List<AbsListener>>();

    //private Comparator<Integer> influenceComparator = Comparator.comparing((i1,i2)->(i1.intValue()-i2));

    public Game() {
        this.bag = new Bag(Constants.INITIAL_BAG_SIZE);
        players = new ArrayList<>();
        playersActionPhase = new ArrayList<>();
        playersPlanningPhase = new ArrayList<>();
        mapMagicianToPlayer = Arrays.stream(Magician.values()).collect(Collectors.toMap(magician -> magician, m->""));
        islandContainer = new IslandContainer();
        clouds = new ArrayList<>();
        characterCards = new ArrayList<>();
        waitingPlayersReconnected = new ArrayList<>();
        initProfessors();
    }

    public void fireInitalState(){
        listeners.firePropertyChange(MAGICIANS_LISTENER, null, mapMagicianToPlayer);
        listeners.firePropertyChange(MODE_LISTENER, null, expertMode);
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
        listeners.firePropertyChange(PROFS_LISTENER, null, professors);

        listeners.firePropertyChange(MOVE_MOTHER_LISTENER, null, motherNature.getPosition());
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
        listeners.firePropertyChange(CLOUDS_LISTENER, null, clouds);
        listeners.firePropertyChange(ROUND_OWNER_LISTENER, null, roundOwner.getNickname());


        listeners.firePropertyChange(PLAYERS_LISTENER, null, buildMapPlayers());

        players.forEach(Player::fireInitialState);

        listeners.firePropertyChange(GAME_STATE_LISTENER, null, GameState.INITIAL_FIRE_COMPLETED);
        listeners.firePropertyChange(GAME_STATE_LISTENER, null, gameState);
    }

    /**
     * Method createListeners creates the Map of listeners.
     * @param client virtualClient - the VirtualClient on the server.
     */
    public void createListeners(VirtualClient client){
        ArrayList<AbsListener> createdListeners = new ArrayList<>();

        createdListeners.add(0,new MoveMotherListener(client, MOVE_MOTHER_LISTENER));
        listeners.addPropertyChangeListener(MOVE_MOTHER_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new IslandsListener(client, ISLANDS_LISTENER));
        listeners.addPropertyChangeListener(ISLANDS_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new CloudsListener(client, CLOUDS_LISTENER));
        listeners.addPropertyChangeListener(CLOUDS_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new ProfsListener(client, PROFS_LISTENER));
        listeners.addPropertyChangeListener(PROFS_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new MagicianListener(client, MAGICIANS_LISTENER));
        listeners.addPropertyChangeListener(MAGICIANS_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new ModeListener(client, MODE_LISTENER));
        listeners.addPropertyChangeListener(MODE_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new CharactersListener(client, CHARACTERS_LISTENER));
        listeners.addPropertyChangeListener(CHARACTERS_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new PlayersStatusListener(client, PLAYERS_LISTENER));
        listeners.addPropertyChangeListener(PLAYERS_LISTENER, createdListeners.get(0));

        // Player listeners
        Optional<Player> player = getPlayerByNickname(client.getNickname());
        player.ifPresent(p -> p.createListeners(client));

        createdListeners.add(0,new RoundOwnerListener(client, ROUND_OWNER_LISTENER));
        listeners.addPropertyChangeListener(ROUND_OWNER_LISTENER, createdListeners.get(0));

        createdListeners.add(0,new GameStateListener(client, GAME_STATE_LISTENER));
        listeners.addPropertyChangeListener(GAME_STATE_LISTENER, createdListeners.get(0));

        clientMapLister.put(client, createdListeners);
    }

    public void removeListeners(VirtualClient client){
        if(clientMapLister.containsKey(client)){
            for(AbsListener listener : clientMapLister.get(client)){
                listeners.removePropertyChangeListener(listener.getPropertyName(), listener);
            }
        }

        // Player listeners
        Optional<Player> player = getPlayerByNickname(client.getNickname());
        player.ifPresent(p -> p.removeListeners(client));
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

    public void createPlayer(int playerID, String nickname) {
        Player p = new Player(playerID, nickname);
        players.add(p);
        listeners.firePropertyChange(PLAYERS_LISTENER, null, buildMapPlayers());
    }

    /**
     * it builds a map with players and only connected players
     * @return
     */
    private Map<String, List<String>> buildMapPlayers(){
        List<String> connectedPlayers = players.stream().filter(Player::isConnected).map(Player::getNickname).toList();
        List<String> allPlayers = players.stream().map(Player::getNickname).toList();
        Map<String, List<String>> map = new HashMap<>();
        map.put(PlayersStatusMessage.CONNECTED_PLAYERS, connectedPlayers);
        map.put(PlayersStatusMessage.PLAYERS, allPlayers);
        return map;
    }

    private Map<String, List<String>> buildMapPlayers(String reConnectedPlayer){
        Map<String, List<String>> map = buildMapPlayers();
        map.put(PlayersStatusMessage.REJOINING_PLAYERS, List.of(reConnectedPlayer));
        return map;
    }

    /**
     * Method chooseMagician() allows players to select an available magician to play with.
     *
     * @param p - the player
     * @param magician - the chosen magician
     */
    public void chooseMagician(Player p, Magician magician) {
        p.setMagician(magician);
        HashMap<Magician, String> oldMagicians = new HashMap<>(mapMagicianToPlayer);
        mapMagicianToPlayer.put(magician, p.getNickname());
        playersActionPhase = players;
        listeners.firePropertyChange(MAGICIANS_LISTENER, null, mapMagicianToPlayer);
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    // This will likely need a MUCH better solution
    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
        listeners.firePropertyChange(MODE_LISTENER, null, expertMode);
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
            listeners.firePropertyChange(PLAYERS_LISTENER, null, buildMapPlayers());
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
        EnumMap<Color, String> oldProfs = new EnumMap<>(this.professors);
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
                if(p.getPlayedCard()!=null && p.isConnected()){    // disconnected player
                    playedCards.add(p.getPlayedCard());
                }

            } else {
                break;
            }
        }
        return playedCards;
    }

    public void removePlayedCards(){
        players.forEach(Player::removePlayedCard);
    }

    /**
     * @return ordered players List for the next planning phase
     */
    public List<Player> getOrderedPlanningPlayers() {
        return new ArrayList<>(playersPlanningPhase);
    }

    public List<Player> getOrderedActionPlayers() {
        return new ArrayList<>(playersActionPhase);
    }

    /**
     * compute the new action order, if there are disconnected player they are put at the end of the turn
     * @return
     */
    public Player setActionOrder() {
        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> {
            int value = 0;
            if(p1.isConnected() && p2.isConnected()) {
                value = p1.getPlayedCard().getValue() - p2.getPlayedCard().getValue();
            }
            else if(!p1.isConnected() && p2.isConnected()){
                value = +1;
            }
            else if(p1.isConnected() && !p2.isConnected()){
                value = -1;
            }
            return value;
        };
        playersActionPhase = new ArrayList<>(playersPlanningPhase);
        playersActionPhase.sort(compareByCardValue);
        return playersActionPhase.get(0);
    }

    /**
     * compute the planning order, it takes in account only the active players
     * @return the first Player for the planning phase
     */
    public Player setPlanningOrder() {
        playersPlanningPhase.clear();
        int playerIndex = players.indexOf(playersActionPhase.get(0));
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get((playerIndex + i) % players.size());
            if(p.isConnected()){
                playersPlanningPhase.add(p);
            }

        }
        return playersPlanningPhase.get(0);
    }


    /**
     * it computes the next player in action phase, if it's end of turn it returns an Empty Optional
     * Warning this method works only if the game is in action phase
     * @param distance is the number of players to skip, e.g. distance 2 it returns not the next but the next next
     * @return
     */
    public Optional<String> getNextPlayerActionPhasePlus(int distance) {
        List<Player> players = playersActionPhase;
        int index = players.indexOf(roundOwner) + distance;
        if (index >= players.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(players.get(index).getNickname());
    }

    /**
     * it computes the next player in action phase, if it's end of turn it returns an Empty Optional
     * Warning this method works only if the game is in action phase
     * @return
     */
    public Optional<String> getNextPlayerActionPhase() {
        return getNextPlayerActionPhasePlus(1);
    }

    /**
     *
     * @deprecated
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
        return mapMagicianToPlayer.entrySet().stream().filter(
                magicianStringEntry -> Objects.equals(magicianStringEntry.getValue(), "")).
                map(Map.Entry::getKey).toList();
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
        listeners.firePropertyChange(GAME_STATE_LISTENER, null , gameState);
        this.gameState = gameState;
    }

    public Player getRoundOwner() {
        return roundOwner;
    }

    public void setRoundOwner(Player roundOwner) {
        String prevRoundOwner = null;
        if(this.roundOwner != null) {
            prevRoundOwner = this.roundOwner.getNickname();
        }
        this.roundOwner = roundOwner;
        listeners.firePropertyChange(ROUND_OWNER_LISTENER, prevRoundOwner, roundOwner.getNickname());
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

    public void activateCharacterCard(int card, Rules rules){
        ArrayList<CharacterCard> oldCharacters = new ArrayList<CharacterCard>(characterCards);
        characterCards.get(card).activate(rules, this);
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
    }
    public void deactivateCharacterCard(int card, Rules rules){
        ArrayList<CharacterCard> oldCharacters = new ArrayList<CharacterCard>(characterCards);
        characterCards.get(card).deactivate(rules, this);
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
    }

    public IslandContainer getIslandContainer() {
        return islandContainer;
    }

    public List<CharacterCard> getActiveCharacters() {
        return characterCards.stream().filter(CharacterCard::isActive).toList();
    }

    public Optional<CharacterCard> getActiveCharacter(Class<? extends CharacterCard> cardType) {
        return characterCards.stream().filter(c -> {return c.isActive() && (cardType.isInstance(c));}).findFirst();
    }

    public int numPlayers() {
        return players.size();
    }
    public int numActivePlayers() {
        return players.stream().filter(Player::isConnected).toList().size();
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

    public void setIslandOwner(int island, String owner){
        islandContainer.setOwner(island, owner);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    public void setIslandBlock(int island, boolean isBlocked){
        islandContainer.setIslandBlocked(island, isBlocked);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    public void joinPrevIsland(int island){
        islandContainer.joinPrevIsland(island);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    public void joinNextIsland(int island){
        islandContainer.joinNextIsland(island);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    public void playCard(Player player, AssistantCard playedCard) {
        player.setAndRemovePlayedCard(playedCard);
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
    public void addPlayerToBeReconnected(String player){
        waitingPlayersReconnected.add(player);
    }

    public void setPlayerConnected(int player, boolean isConnected){
        getPlayerByID(player).setConnected(isConnected);
        listeners.firePropertyChange(PLAYERS_LISTENER, null, isConnected ?
                buildMapPlayers(getPlayerByID(player).getNickname()) : buildMapPlayers());
    }

    public List<String> getWaitingPlayersReconnected(){
        return new ArrayList<>(waitingPlayersReconnected);
    }
    public void reEnterWaitingPlayers(){
        for(Player p : players){
            if(waitingPlayersReconnected.contains(p.getNickname())){
                setPlayerConnected(p.getID(), true);
            }
        }
        waitingPlayersReconnected.clear();
    }
}
