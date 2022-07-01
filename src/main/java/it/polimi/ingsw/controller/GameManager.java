package it.polimi.ingsw.controller;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.characters.*;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.constants.TowerColor;
import it.polimi.ingsw.model.islands.BaseIsland;
import it.polimi.ingsw.model.islands.Island;
import it.polimi.ingsw.server.GameHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * GameManager represent the main Controller class of the Game.
 * It initializes the Game Model and executes all the actions on it.
 */
public class GameManager implements PropertyChangeListener {

    public static final String ROUND_CONTROLLER = "roundController";
    private final Game game;
    private final Rules rules;
    private final GameHandler gameHandler;
    private RoundManager roundManager;
    private final PropertyChangeSupport controllerListeners = new PropertyChangeSupport(this);

    /**
     * Constructor GameManager creates the RoundManager and Rules instance and sets the current game.
     *
     * @param game        the current game instance.
     * @param gameHandler the game's GameHandler instance.
     * @see GameHandler
     */
    public GameManager(Game game, GameHandler gameHandler) {
        this.game = game;
        rules = new Rules();
        this.roundManager = new RoundManager(this);
        this.roundManager.addListener(this);
        this.gameHandler = gameHandler;
    }

    /**
     * Method getGame returns the current game instance.
     *
     * @see Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Method getGameHandler returns the current game's GameHandler.
     *
     * @see GameHandler
     */
    protected GameHandler getGameHandler() {
        return gameHandler;
    }

    /**
     * Method getRules return the current game rules.
     *
     * @see Rules
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Method initGame calls all the sub-methods to properly initialize the Game Model,
     * also regarding the expert game mode if present.
     * The method is responsible also for sending the initial state of the Game to the clients.
     */
    public void initGame() {
        initMotherNature();
        initIslands();
        fillBag();
        initSchools();
        initClouds();
        initRoundManager();
        if (game.isExpertMode()) {
            initCharacters();
            game.initBalance(Constants.NUM_COINS);
            initPlayersBalance();
        }
        game.fireInitialState();
        game.setGameState(GameState.SETUP_CHOOSE_MAGICIAN);
    }

    /**
     * Method initCharacters creates all 12 characters present in the Game,
     * and then it randomly sets 3 of them to the current Game.
     *
     * @see Game#initCharacterCards(List)
     */
    private void initCharacters() {
        List<CharacterCard> characters = new ArrayList<>();
        characters.add(new Centaur());
        characters.add(new Farmer());
        characters.add(new Herald());
        characters.add(new Joker( game.getBag()));
        characters.add(new Knight());
        characters.add(new Thief());
        characters.add(new Mushroom());
        characters.add(new Postman());
        characters.add(new Princess(game.getBag()));
        characters.add(new Minstrel());
        characters.add(new Monk(game.getBag()));
        characters.add(new Grandma());

        Collections.shuffle(characters);
        List<CharacterCard> extractedCharacters = characters.subList(0, 3);
        extractedCharacters.forEach(CharacterCard::init);
        ArrayList<CharacterCard> gameCharacters = new ArrayList<>(characters.subList(0, 3));
        game.initCharacterCards(gameCharacters);
    }

    /**
     * Method initSchool creates the basic instance of a School to each of the players present in the game.
     *
     * @see School#School(int, TowerColor, Map)
     */
    private void initSchools() {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            // create and fill the school
            Map<Color, Integer> students = game.getBag().extract(Rules.getEntrySize(players.size()));
            TowerColor towerColor = TowerColor.values()[i];
            School school = new School(Rules.getTowersPerPlayer(players.size()), towerColor, students);
            player.setSchool(school);
        }
    }

    /**
     * Method initRoundManager initializes the RoundManager instance, that manages the handling of the game's turns.
     */
    private void initRoundManager() {
        game.setPlayersActionPhase(game.getPlayers());
        game.setRoundOwner(game.getPlayers().get(0));
        game.setGameState(GameState.GAME_ROOM);
    }

    /**
     * Method initIslands creates 12 islands, sets a random student on them, and then it sets them to the Game Model.
     *
     * @see Island#addStudent(Color)
     * @see Game#initIslands(LinkedList)
     */
    private void initIslands() {
        int motherNaturePosition = game.getMotherNature().getPosition();
        int opposite = (motherNaturePosition + 6) % 12;
        LinkedList<Island> islands = new LinkedList<>();

        for (int i = 0; i < Constants.MAX_ISLANDS; i++) {
            Island island = new BaseIsland();
            if (i != opposite && i != motherNaturePosition) {
                island.addStudent(game.getBag().extract());
            }
            islands.add(island);
        }
        game.initIslands(islands);
    }

    /**
     * Method initMotherNature sets the MotherNature position. This is done with a random value between 0 and 11.
     *
     * @see Game#initMotherNature(MotherNature)
     */
    private void initMotherNature() {
        Random rand = new Random();
        int motherNaturePosition = rand.nextInt(0, Constants.MAX_ISLANDS);
        MotherNature motherNature = new MotherNature(motherNaturePosition);
        game.initMotherNature(motherNature);
    }

    /**
     * Method initPlayersBalance gives to every the player in the game his initial game coins, taken from the game.
     *
     * @see Game#incrementPlayerBalance(String)
     */
    private void initPlayersBalance() {
        for (Player p : game.getPlayers()) {
            for (int i = 0; i < Constants.INITIAL_PLAYER_BALANCE; i++) {
                game.incrementPlayerBalance(p.getNickname());
            }
        }
    }

    /**
     * Method fillBag fills the Game Bag to the required size as specified by the game's rules.
     *
     * @see Bag#extendBag(int)
     */
    private void fillBag() {
        game.getBag().extendBag(Constants.BAG_SIZE - Constants.INITIAL_BAG_SIZE);
    }

    /**
     * Method initClouds initializes the required clouds, and then it sets them to the game,
     * and it refills them with the required student amount.
     *
     * @see Game#initClouds(LinkedList)
     * @see Game#refillClouds()
     */
    private void initClouds() {
        int numClouds = game.getPlayers().size();
        LinkedList<Cloud> clouds = new LinkedList<>();
        for (int i = 0; i < numClouds; i++) {
            Cloud cloud = new Cloud();
            clouds.add(cloud);
        }
        game.initClouds(clouds);
        game.refillClouds();
    }

    /**
     * Method performAction calls the underlying method on the RoundManager.
     *
     * @param action the Performable to be executed on the Game Model.
     * @throws InvalidPlayerException if the action's player is not valid.
     * @throws RoundOwnerException    if the action's player is not the current round owner.
     * @throws GameException          if a generic error is thrown.
     * @see RoundManager#performAction(Performable)
     * @see Performable
     */
    public void performAction(Performable action) throws InvalidPlayerException, RoundOwnerException, GameException {
        roundManager.performAction(action);
    }

    /**
     * Method propertyChange notifies the controller listeners.
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        controllerListeners.firePropertyChange(evt);
    }

    /**
     * Method handleNewRoundOwnerOnDisconnect handles the disconnection of the current round owner,
     *
     * @param username the username of the disconnected round owner.
     * @see RoundManager#handleNewRoundOwnerOnDisconnect(String)
     */
    public void handleNewRoundOwnerOnDisconnect(String username) {
        roundManager.handleNewRoundOwnerOnDisconnect(username);
    }
}