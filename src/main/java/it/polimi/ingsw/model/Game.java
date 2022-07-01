package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.constants.Magician;
import it.polimi.ingsw.model.cards.characters.Grandma;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.model.islands.IslandContainer;
import it.polimi.ingsw.server.answers.model.PlayersStatusMessage;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.listeners.*;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Game class contains the main Model (MVC) logic of "Eriantys".
 * The class is responsible for the initialization of the game Model,
 * for certain Model and connection status changes,
 * and for the update of the data on the clients.
 */
public class Game {

    /**
     * These are all the listeners put on the Model, to fire all the small changes to the client
     */
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
    /**
     * Ordered list of players for the action phase
     */
    private List<Player> playersActionPhase;
    /**
     * Ordered list of players for the planning phase
     */
    private List<Player> playersPlanningPhase;
    /**
     * List of players to be re-inserted in the game
     */
    private List<String> waitingPlayersReconnected;
    private boolean expertMode;
    private IslandContainer islandContainer;
    private MotherNature motherNature;
    private EnumMap<Color, String> professors;
    private List<CharacterCard> characterCards;
    /**
     * Size is 3 for the expert mode and 0 in the normal game mode.
     */
    private GameState gameState;
    private Player roundOwner;
    protected final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private final HashMap<VirtualClient, List<AbsListener>> clientMapLister = new HashMap<VirtualClient, List<AbsListener>>();

    /**
     * Constructor Game provides the basic game setup.
     * Other setup is needed for the game to be playable.
     */
    public Game() {
        this.bag = new Bag(Constants.INITIAL_BAG_SIZE);
        players = new ArrayList<>();
        playersActionPhase = new ArrayList<>();
        playersPlanningPhase = new ArrayList<>();
        mapMagicianToPlayer = Arrays.stream(Magician.values()).collect(Collectors.toMap(magician -> magician, m -> ""));
        islandContainer = new IslandContainer();
        clouds = new ArrayList<>();
        characterCards = new ArrayList<>();
        waitingPlayersReconnected = new ArrayList<>();
        initProfessors();
    }

    /**
     * Method fireInitialState is responsible for forwarding the initial or current game state if a client connects.
     *
     * @see Player#fireInitialState()
     */
    public void fireInitialState() {
        listeners.firePropertyChange(MAGICIANS_LISTENER, null, mapMagicianToPlayer);
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
     *
     * @param client the VirtualClient on the server.
     */
    public void createListeners(VirtualClient client) {
        ArrayList<AbsListener> createdListeners = new ArrayList<>();

        createdListeners.add(0, new MoveMotherListener(client, MOVE_MOTHER_LISTENER));
        listeners.addPropertyChangeListener(MOVE_MOTHER_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new IslandsListener(client, ISLANDS_LISTENER));
        listeners.addPropertyChangeListener(ISLANDS_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new CloudsListener(client, CLOUDS_LISTENER));
        listeners.addPropertyChangeListener(CLOUDS_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new ProfsListener(client, PROFS_LISTENER));
        listeners.addPropertyChangeListener(PROFS_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new MagicianListener(client, MAGICIANS_LISTENER));
        listeners.addPropertyChangeListener(MAGICIANS_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new ModeListener(client, MODE_LISTENER));
        listeners.addPropertyChangeListener(MODE_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new CharactersListener(client, CHARACTERS_LISTENER));
        listeners.addPropertyChangeListener(CHARACTERS_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new PlayersStatusListener(client, PLAYERS_LISTENER));
        listeners.addPropertyChangeListener(PLAYERS_LISTENER, createdListeners.get(0));

        // Player listeners
        Optional<Player> player = getPlayerByNickname(client.getNickname());
        player.ifPresent(p -> p.createListeners(client));

        createdListeners.add(0, new RoundOwnerListener(client, ROUND_OWNER_LISTENER));
        listeners.addPropertyChangeListener(ROUND_OWNER_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new GameStateListener(client, GAME_STATE_LISTENER));
        listeners.addPropertyChangeListener(GAME_STATE_LISTENER, createdListeners.get(0));

        clientMapLister.put(client, createdListeners);
    }

    /**
     * Method removeListeners removes a disconnected client from the list of listeners.
     *
     * @param client the VirtualClient on the server.
     */
    public void removeListeners(VirtualClient client) {
        if (clientMapLister.containsKey(client)) {
            for (AbsListener listener : clientMapLister.get(client)) {
                listeners.removePropertyChangeListener(listener.getPropertyName(), listener);
            }
        }
        // Player listeners
        Optional<Player> player = getPlayerByNickname(client.getNickname());
        player.ifPresent(p -> p.removeListeners(client));
    }

    /**
     * Method initProfessor is responsible for the creation of the starting professor map
     * (a professor doesn't have an owner yet).
     */
    public void initProfessors() {
        professors = new EnumMap<Color, String>(Color.class);
        for (Color c : Color.values()) {
            // set 1 professor per color, initial state
            professors.put(c, null);
        }
    }

    /**
     * Method initClouds is a setter method for the cloud list.
     *
     * @param clouds the cloud List to be set to the game
     */
    public void initClouds(LinkedList<Cloud> clouds) {
        //new object create with the same elements
        this.clouds.removeAll(clouds);
        this.clouds.addAll(clouds);
    }

    /**
     * Method initMotherNature is a setter method for the MotherNature object. This method also fires any changes to the clients.
     *
     * @param motherNature the MotherNature instance to be set to the game
     */
    public void initMotherNature(MotherNature motherNature) {
        this.motherNature = motherNature;
        listeners.firePropertyChange(MOVE_MOTHER_LISTENER, null, motherNature.getPosition());
    }

    /**
     * Method initIslands creates an islandContainer object with a list of islands. This method also fires any changes to the clients.
     *
     * @param islands the list of islands to be set to the container
     */
    public void initIslands(LinkedList<Island> islands) {
        this.islandContainer = new IslandContainer(islands);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    /**
     * Method initCharacterCards is a setter method for the character cards.
     *
     * @param cards the list of cards to be set
     */
    public void initCharacterCards(List<CharacterCard> cards) {
        this.characterCards = cards;
    }

    /**
     * Method initBalance is a setter method the initial balance in the game.
     *
     * @param balance the initial balance to be set.
     */
    public void initBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Method incrementPlayerBalance increments player balance and decreases the game's balance.
     *
     * @param nickname the selected player's username.
     */
    public void incrementPlayerBalance(String nickname) {
        getPlayerByNickname(nickname).ifPresent(player -> {
            if (balance > 0) {
                player.addCoin();
                balance--;
            }
        });
    }

    /**
     * Method decrementBalance decrements the game's balance.
     */
    public void decrementBalance() {
        balance--;
    }

    /**
     * Method incrementBalance increments the game's balance by a specified value.
     *
     * @param value the number to be added to the game's balance.
     */
    public void incrementBalance(int value) {
        balance += value;
    }

    /**
     * Method getBalance returns the current game balance.
     *
     * @return the current game's balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Method isNicknameTaken checks if the username is already taken by another player.
     *
     * @param username the name to be checked
     * @return true if the nickname is taken, false if it is available
     */
    private boolean isNicknameTaken(String username) {
        return getPlayerByNickname(username).isPresent();
    }

    /**
     * method createPlayer creates a new Player instance with a specific ID and username,
     * then it adds the player to the game's player list and fires the changes
     *
     * @param playerID the ID to be set to the new player
     * @param username the username to be set to the new player
     */
    public void createPlayer(int playerID, String username) {
        Player p = new Player(playerID, username);
        players.add(p);
        listeners.firePropertyChange(PLAYERS_LISTENER, null, buildMapPlayers());
    }

    /**
     * Method buildMapPlayers creates and returns a map with all the players and only the connected players.
     */
    private Map<String, List<String>> buildMapPlayers() {
        List<String> activePlayers = players.stream().filter(Player::isActive).map(Player::getNickname).toList();
        List<String> allPlayers = players.stream().map(Player::getNickname).toList();
        Map<String, List<String>> map = new HashMap<>();
        map.put(PlayersStatusMessage.ACTIVE_PLAYERS, activePlayers);
        map.put(PlayersStatusMessage.PLAYERS, allPlayers);
        return map;
    }

    /**
     * Method buildMapPlayers creates and returns a map with all the players, the connected players, and the player that has reconnected.
     *
     * @param reConnectedPlayer - the username of the player that has reconnected
     */
    private Map<String, List<String>> buildMapPlayers(String reConnectedPlayer) {
        Map<String, List<String>> map = buildMapPlayers();
        map.put(PlayersStatusMessage.REJOINING_PLAYERS, List.of(reConnectedPlayer));
        return map;
    }

    /**
     * Method chooseMagician allows players to select an available magician to play with.
     *
     * @param p        the player
     * @param magician the chosen magician
     */
    public void chooseMagician(Player p, Magician magician) {
        p.setMagician(magician);
        mapMagicianToPlayer.put(magician, p.getNickname());
        playersActionPhase = players;
        listeners.firePropertyChange(MAGICIANS_LISTENER, null, mapMagicianToPlayer);
    }

    /**
     * Method isExpertMode returns if the expert mode has been selected.
     *
     * @return true if the expert mode has been selected, false otherwise.
     */
    public boolean isExpertMode() {
        return expertMode;
    }

    /**
     * Method setExpertMode is a setter method for the chosen game mode.
     *
     * @param expertMode - the first player selection, true if the expert mode is selected, false otherwise.
     */
    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
        listeners.firePropertyChange(MODE_LISTENER, null, expertMode);
    }

    /**
     * Method moveMotherNature changes the position of motherNature.
     *
     * @param deltaPositions the specified movement delta.
     */
    public void moveMotherNature(int deltaPositions) {
        int oldPosition = motherNature.getPosition();
        int newPosition = (oldPosition + deltaPositions + islandContainer.size()) % islandContainer.size();
        motherNature.setIsland(newPosition);
        listeners.firePropertyChange(MOVE_MOTHER_LISTENER, oldPosition, newPosition);
    }

    /**
     * Method setPlayersActionPhase sets the ordered list of players for the action phase
     *
     * @param playersActionPhase - the list to be set.
     */
    public void setPlayersActionPhase(List<Player> playersActionPhase) {
        this.playersActionPhase = playersActionPhase;
    }

    /**
     * Method setProfessor is a setter method for the professor ownership map.
     * The method then fires any changes.
     *
     * @param professors the updated map to be set to the game
     */
    public void setProfessors(EnumMap<Color, String> professors) {
        EnumMap<Color, String> oldProfs = new EnumMap<>(this.professors);
        this.professors = professors;
        listeners.firePropertyChange(PROFS_LISTENER, oldProfs, professors);
    }

    /**
     * Method getPlayedCards returns the cards played until the current player (round owner).
     *
     * @return a list of the type AssistantCard
     * @see it.polimi.ingsw.controller.actions.PlayCard
     */
    public List<AssistantCard> getPlayedCards() {
        ArrayList<AssistantCard> playedCards = new ArrayList<AssistantCard>();
        for (Player p : getOrderedPlanningPlayers()) {
            if (!p.equals(roundOwner)) {
                if (p.getPlayedCard() != null && p.isActive()) {    // disconnected player
                    playedCards.add(p.getPlayedCard());
                }

            } else {
                break;
            }
        }
        return playedCards;
    }

    /**
     * Method removePlayedCards removes all the played cards in order for the next planning phase to begin.
     *
     * @see it.polimi.ingsw.controller.RoundManager
     */
    public void removePlayedCards() {
        players.forEach(Player::removePlayedCard);
    }

    /**
     * Method getOrderedPlanningPlayers returns the ordered players list for the next planning phase.
     */
    public List<Player> getOrderedPlanningPlayers() {
        return new ArrayList<>(playersPlanningPhase);
    }

    /**
     * Method getOrderedActionPlayers returns the ordered players list for the next action phase.
     */
    public List<Player> getOrderedActionPlayers() {
        return new ArrayList<>(playersActionPhase);
    }

    /**
     * Method setActionOrder orders the player list based on the planning phase cards for the next action phase.
     * It also checks if the player is currently connected.
     *
     * @return the first player of the action phase.
     */
    public Player setActionOrder() {
        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> {
            int value = 0;
            if (p1.isActive() && p2.isActive()) {
                value = p1.getPlayedCard().getValue() - p2.getPlayedCard().getValue();
            } else if (!p1.isActive() && p2.isActive()) {
                value = 1;
            } else if (p1.isActive() && !p2.isActive()) {
                value = -1;
            }
            return value;
        };
        playersActionPhase = new ArrayList<>(playersPlanningPhase);
        playersActionPhase.sort(compareByCardValue);
        return playersActionPhase.get(0);
    }

    /**
     * Method setPlanningOrder orders the player list based on the last planning phase cards for the next planning phase.
     * It also takes account if a player is currently connected.
     *
     * @return the first player of the planning phase.
     */
    public Player setPlanningOrder() {
        playersPlanningPhase.clear();
        int playerIndex = players.indexOf(playersActionPhase.get(0));
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get((playerIndex + i) % players.size());
            if (p.isActive()) {
                playersPlanningPhase.add(p);
            }

        }
        return playersPlanningPhase.get(0);
    }


    /**
     * Method getNextPlayerActionPhasePlus computes the next player in action phase, if it's the end of the turn it returns empty Optional type.
     * This method only works if the game is set to the action phase.
     *
     * @param distance is the number of players to skip.
     * @return the next player at the specified distance, if there is none, it returns empty.
     */
    public Optional<String> getNextPlayerActionPhase(int distance) {
        List<Player> players = playersActionPhase;
        int index = players.indexOf(roundOwner) + distance;
        if (index >= players.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(players.get(index).getNickname());
    }

    /**
     * Method getNextPlayerActionPhase computes the next player in action phase, if it's the end of the turn it returns empty Optional type.
     * This method only works if the game is set to the action phase.
     *
     * @see Game#getNextPlayerActionPhase(int distance)
     */
    public Optional<String> getNextPlayerActionPhase() {
        return getNextPlayerActionPhase(1);
    }

    /**
     * Method getAvailableMagicians returns the list of currently available magicians.
     */
    public List<Magician> getAvailableMagicians() {
        // Filter the magicians based on the magicians present in the Magician - Player map

        return Magician.orderMagicians(mapMagicianToPlayer.entrySet().stream().filter(
                        magicianStringEntry -> Objects.equals(magicianStringEntry.getValue(), "")).
                map(Map.Entry::getKey).toList());
    }

    /**
     * Method getPlayers returns all the players present in the game.
     *
     * @return The list of players in the game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Method getActivePlayers returns a list of only the currently connected players.
     *
     * @return The list of the connected players.
     */
    public List<Player> getActivePlayers() {
        return players.stream().filter(Player::isActive).collect(Collectors.toList());
    }

    /**
     * Method getGameState returns the current game state.
     *
     * @see GameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Method setGameState sets the provided game state to the game and fires the changes to the clients.
     *
     * @param gameState the GameState to be set.
     * @see GameState
     */
    public void setGameState(GameState gameState) {
        listeners.firePropertyChange(GAME_STATE_LISTENER, null, gameState);
        this.gameState = gameState;
    }

    /**
     * Method getRoundOwner returns the current "round owner" (the player that is currently able to play).
     */
    public Player getRoundOwner() {
        return roundOwner;
    }

    /**
     * Method setRoundOwner sets the current "round owner" (the player that is currently able to play).
     *
     * @param roundOwner The Player to be set to the round owner role.
     */
    public void setRoundOwner(Player roundOwner) {
        String prevRoundOwner = null;
        if (this.roundOwner != null) {
            prevRoundOwner = this.roundOwner.getNickname();
        }
        this.roundOwner = roundOwner;
        listeners.firePropertyChange(ROUND_OWNER_LISTENER, prevRoundOwner, roundOwner.getNickname());
    }

    /**
     * Method getProfessors returns the ownership Map of the professors of the game.
     * The map is structures as Color (the color of the professor) - String (the nickname of the owner).
     */
    public EnumMap<Color, String> getProfessors() {
        return professors;
    }

    /**
     * Method getMotherNature returns the reference to the MotherNature Object.
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     * Method getClouds returns the reference to the game's clouds.
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Method getBag returns the reference to the game's bag.
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Method getCharacterCards returns the reference to the game's list of character cards.
     */
    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Method activateCharacterCard activates a specified character card and fires the changes to the clients.
     *
     * @param card  the index of the card to activate.
     * @param rules the current game rules.
     */
    public void activateCharacterCard(int card, Rules rules) {
        characterCards.get(card).activate(rules, this);
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
    }

    /**
     * Method deactivateCharacterCard deactivates a specified character card and fires the changes to the clients.
     *
     * @param card  the index of the card to deactivate.
     * @param rules the current game rules.
     */
    public void deactivateCharacterCard(int card, Rules rules) {
        characterCards.get(card).deactivate(rules, this);
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
    }

    /**
     * Method addBlockingCardsToGrandma increase the number of blocking cards in the grandma character
     *
     * @param grandma the reference to the grandma card.
     */
    public void addBlockingCardsToGrandma(Grandma grandma) {
        grandma.addBlockingCard();
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
    }

    /**
     * Method addBlockingCardsToGrandma increase the number of blocking cards in the grandma character
     *
     * @param grandma the reference to the grandma card.
     */
    public void removeBlockingCardToGrandma(Grandma grandma) {
        grandma.removeBlockingCard();
        listeners.firePropertyChange(CHARACTERS_LISTENER, null, characterCards);
    }

    /**
     * Method getIslandContainer returns the reference to the IslandContainer instance.
     *
     * @see IslandContainer
     */
    public IslandContainer getIslandContainer() {
        return islandContainer;
    }

    /**
     * Method getActiveCharacter returns the currently active character card.
     *
     * @param cardType the class reference to the card to check
     * @return the reference to the character card if present, empty otherwise.
     */
    public Optional<CharacterCard> getActiveCharacter(Class<? extends CharacterCard> cardType) {
        return characterCards.stream().filter(c -> {
            return c.isActive() && (cardType.isInstance(c));
        }).findFirst();
    }

    /**
     * Method numPlayers returns the number of the players in the game (connected or not).
     */
    public int numPlayers() {
        return players.size();
    }

    /**
     * Method numActivePlayers returns the number of connected players in the game.
     */
    public int numActivePlayers() {
        return players.stream().filter(Player::isActive).toList().size();
    }

    /**
     * Method refillClouds is responsible for refilling the students on the cloud after the last cloud is emptied.
     */
    public void refillClouds() {
        for (Cloud c : clouds) {
            c.pickStudents(); //sure of to empty the cloud
            c.addStudents(bag.extract(Rules.getStudentsPerTurn(numPlayers()))); //refill the same cloud
        }
        listeners.firePropertyChange(CLOUDS_LISTENER, null, clouds);
    }

    /**
     * Method pickClouds lets a user select a cloud, empties the cloud and returns the Map of the students on the clouds.
     *
     * @param cloudIndex the specified cloud index.
     * @return The map of the selected cloud's students.
     * @throws IndexOutOfBoundsException if the index selected does not correspond to any cloud.
     */
    public Map<Color, Integer> pickCloud(int cloudIndex) throws IndexOutOfBoundsException {
        Cloud cloud = clouds.get(cloudIndex);
        Map<Color, Integer> students = cloud.pickStudents();
        listeners.firePropertyChange(CLOUDS_LISTENER, null, clouds);
        return students;
    }

    /**
     * Method addIslandStudent adds a student to the specified island.
     *
     * @param islandIndex the specified island index.
     * @param student     the student to be added.
     */
    public void addIslandStudent(int islandIndex, Color student) {
        islandContainer.addIslandStudent(islandIndex, student);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    /**
     * Method setIslandOwner sets the ownership of an island to a specified player.
     *
     * @param island the specified island index.
     * @param owner  the owner to be set, identified by his username.
     */
    public void setIslandOwner(int island, String owner) {
        islandContainer.setOwner(island, owner);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    /**
     * Method setIslandBlock sets the blocked flag to a certain island. This is related to the Grandma character card.
     *
     * @param island    the selected island index.
     * @param isBlocked the blocking status to be set.
     * @see it.polimi.ingsw.model.cards.characters.Grandma
     */
    public void setIslandBlock(int island, boolean isBlocked) {
        islandContainer.setIslandBlocked(island, isBlocked);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    /**
     * Method joinPrevIsland joins the current island with the previous one.
     *
     * @param island the current island index.
     * @see IslandContainer
     */
    public void joinPrevIsland(int island) {
        islandContainer.joinPrevIsland(island);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    /**
     * Method joinNextIsland joins the current island with the next one.
     *
     * @param island the current island index.
     * @see IslandContainer
     */
    public void joinNextIsland(int island) {
        islandContainer.joinNextIsland(island);
        listeners.firePropertyChange(ISLANDS_LISTENER, null, islandContainer);
    }

    /**
     * Method playCard executes a play card action,
     * defined by the removal of the card from the player's and the setting of the old card to the played cards.
     *
     * @param player     the current player username.
     * @param playedCard the card to be played.
     */
    public void playCard(Player player, AssistantCard playedCard) {
        player.setAndRemovePlayedCard(playedCard);
    }

    /**
     * Method getPlayerByNickname returns the player reference from its nickname,
     * if there isn't any player with that nickname it returns the Optional.Empty value.
     *
     * @param username the selected player's username
     * @return the player reference if present, Optional.Empty otherwise
     */
    public Optional<Player> getPlayerByNickname(String username) { //getting the object player by nickname
        return players.stream().filter(player -> player.getNickname().equals(username)).findFirst();
    }

    /**
     * Method getPlayerByNickname returns the player reference from its ID.
     * If a player is not player, it returns null.
     *
     * @param id the selected player's ID.
     * @return the player reference if present, null otherwise.
     */
    public Player getPlayerByID(int id) {
        for (Player player : players) {
            if (player.getID() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * Method addPlayerToBeReconnected adds the player to the reconnected list
     * if a player reconnects after having left the game.
     *
     * @param player the player's username.
     */
    public void addPlayerToBeReconnected(String player) {
        waitingPlayersReconnected.add(player);
    }

    /**
     * Method setPlayerConnected sets the player's connection status.
     *
     * @param player      the player's ID.
     * @param isConnected true if connected, false otherwise
     */
    public void setPlayerConnected(int player, boolean isConnected) {
        getPlayerByID(player).setConnected(isConnected);
        listeners.firePropertyChange(PLAYERS_LISTENER, null, isConnected ?
                buildMapPlayers(getPlayerByID(player).getNickname()) : buildMapPlayers());
    }

    /**
     * Method getWaitingPlayersReconnected returns the list of the player to be reconnected to the game.
     */
    public List<String> getWaitingPlayersReconnected() {
        return new ArrayList<>(waitingPlayersReconnected);
    }

    /**
     * Method reEnterWaitingPlayers moves the players from the reconnected queue to the actual game.
     */
    public void reEnterWaitingPlayers() {
        for (Player p : players) {
            if (waitingPlayersReconnected.contains(p.getNickname())) {
                setPlayerConnected(p.getID(), true);
            }
        }
        waitingPlayersReconnected.clear();
    }
}