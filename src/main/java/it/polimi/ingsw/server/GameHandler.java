package it.polimi.ingsw.server;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.answers.*;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * GameHandler class handles a single match, instantiating a Game and a main GameManager.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 */
public class GameHandler {

    private static final String PLAYER = "Player";
    private final Server server;
    private final GameManager controller;
    private final Game game;
    private final PropertyChangeSupport controllerListener = new PropertyChangeSupport(this);
    private boolean isStarted;
    private boolean isEnded;
    private int playersNumber;
    Timer timer = new Timer("WinningTimer"); // to decree victory in case of 1 player remaining

    /**
     * Constructor GameHandler creates a new GameHandler instance.
     *
     * @param server the main server instance.
     */
    public GameHandler(Server server) {
        this.server = server;
        isStarted = false;
        isEnded = false;
        game = new Game();
        controller = new GameManager(game, this);
        controllerListener.addPropertyChangeListener(controller);
    }

    /**
     * Method createPlayer calls the Model player creator when a new player joins a game.
     *
     * @param nickname the name chosen by the joining player.
     * @param playerID the ID that the server has given to him.
     */
    public void createPlayer(String nickname, int playerID) {
        game.createPlayer(playerID, nickname);
    }

    /**
     * Method reEnterPlay lets a player join a game that he has previously left.
     *
     * @param nickname the name of the player that has joined.
     */
    public void reEnterPlayer(String nickname) {
        game.addPlayerToBeReconnected(nickname);
        game.createListeners(server.getClientByID(server.getIDByNickname(nickname)));
        game.fireInitialState();
        if (game.numActivePlayers() == 1) {
            reEnterWaitingPlayers();
        }
    }

    /**
     * Method reEnterWaitingPlayers adds player that have reconnected to the game in order for the game to continue.
     */
    public void reEnterWaitingPlayers() {
        stopWinningTimer();
        game.getWaitingPlayersReconnected().forEach(p -> sendAll(new CustomMessage(p + " is back in the play")));
        game.reEnterWaitingPlayers();
    }

    /**
     * Method sendAll sends an incoming packaged message to all the clients connected to the game.
     *
     * @param message the message to forward to all the players.
     */
    public void sendAll(Answer message) {
        for (Player p : game.getPlayers()) {
            server.getClientByID(p.getID()).send(message);
        }
    }

    /**
     * Method sendAllExcept sends an incoming packaged message to all the clients connected to the game,
     * except the excluded one.
     *
     * @param message the message to forward to all but one player.
     */
    public void sendAllExcept(Answer message, int excludedID) {
        for (Player p : game.getActivePlayers()) {
            if (server.getIDByNickname(p.getNickname()) != excludedID) {
                server.getClientByID(p.getID()).send(message);
            }
        }
    }

    /**
     * Method setPlayersNumber assigns the game's player number (decided at the game setup phase).
     *
     * @param number the player number to set.
     */
    public void setPlayersNumber(int number) {
        playersNumber = number;
    }

    /**
     * Method setExpertMode assigns the game's game (decided at the game setup phase).
     *
     * @param expert the flag that represents if a game is to be to the normal or expert game mode.
     */
    public void setExpertMode(boolean expert) {
        game.setExpertMode(expert);
    }

    /**
     * Method isStarted checks if a game has started.
     *
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Method isEnded checks if a game has ended.
     *
     * @return true if the game has ended, false otherwise.
     */
    public synchronized boolean isEnded() {
        return isEnded;
    }

    /**
     * Method isSetupPhase checks if a game is currently in the setup phase.
     *
     * @return true if the game is the setup phase, false otherwise.
     */
    public boolean isSetupPhase() {
        return GameState.SETUP_CHOOSE_MAGICIAN.equals(game.getGameState());
    }

    // TODO
    public List<Player> getActivePlayers(){
        return game.getActivePlayers();
    }

    /**
     * Method setEnded ends a currently not ended game to the ended status.
     */
    public synchronized void setEnded() {
        isEnded = true;
    }

    /**
     * Method startGame initializes the main controller class, which in turn initializes the model class.
     * This method is needed for a game to begin.
     */
    public void startGame() throws InterruptedException {
        // add listeners for all players to the model
        for (Player p : game.getPlayers()) {
            game.createListeners(server.getClientByID(p.getID()));
        }
        controller.initGame();
        for (int i = 3; i > 0; i--) {
            sendAll(new CustomMessage("Match starting in " + i));
            TimeUnit.MILLISECONDS.sleep(500);
        }
        sendAll(new CustomMessage("The match has started!"));
        isStarted = true;
    }

    /**
     * Method endGame sets a game to the ended status, terminating connections to all the connected clients.
     * This method is needed for a game to end.
     */
    public void endGame() {
        setEnded();
        game.setGameState(GameState.GAME_ENDED);
        while (!game.getActivePlayers().isEmpty()) {
            server.getClientByID(game.getActivePlayers().get(0).getID()).getConnection().close();
        }
    }

    /**
     * Method endGame sets a game to the ended status, terminating connections to all the connected clients.
     * This method represents when a player leaves in the setup phase. Other disconnection is handled differently.
     *
     * @param leftNickname the name of the player that has left.
     * @see GameHandler#reEnterPlayer(String)
     */
    public synchronized void endGame(String leftNickname) { // TODO check synchronized
        stopWinningTimer();
        sendAll(new ConnectionMessage(PLAYER + " " + leftNickname + " left the game, the match will now end." +
                "\nThanks for playing!", false));

        while (!game.getActivePlayers().isEmpty()) {
            server.getClientByID(game.getActivePlayers().get(0).getID()).getConnection().close();
        }
        for(Player p : game.getPlayers()){
            server.unregisterClient(p.getID());
        }
    }

    /**
     * Method unregisterPlayer lets a player leave the game. If the game is set to the setup phase, the game then ends.
     *
     * @param id          the ID of the player that is leaving.
     */
    public synchronized void unregisterPlayer(int id) {
        game.setPlayerConnected(id, false);
        boolean isGameEnded = isEnded() || !isStarted() || isSetupPhase() || getActivePlayers().size() == 0
                || game.getGameState() == GameState.SETUP_CHOOSE_MAGICIAN;
        game.removeListeners(server.getClientByID(id));
        if (isGameEnded) {
            endGame(server.getNicknameByID(id));
        } else {
            controller.handleNewRoundOwnerOnDisconnect(server.getNicknameByID(id));
            startWinningTimer();
        }
    }

    /**
     * Method sendVictory send the win message to the player that has won, and to the other players that they have lost.
     *
     * @param player the name of the player that has won.
     */
    public void sendVictory(String player) {
        sendAll(new WinMessage(player));
        endGame();
    }

    /**
     * Method startWinningTimer starts a timer when a game is left with only one player.
     * When the timer ends with no reconnection by any player, the last connected player wins.
     */
    private void startWinningTimer() {
        if (game.numActivePlayers() == 1 && !isEnded()) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (game.getActivePlayers().size() > 0) {
                        sendAll(new CustomMessage("You WON, you are the only player and are passed " + Constants.DELAY_WINNING_TIMER + " seconds"));
                        sendAll(new WinMessage(game.getPlayers().stream().filter(Player::isActive).findFirst().get().getNickname()));
                        endGame();
                    }
                }
            };
            timer.schedule(timerTask, Constants.DELAY_WINNING_TIMER * 1000);
        }
    }

    /**
     * Method stopWinningTimer stops a timer when a game with only one player left
     * is joined by a previously disconnected one.
     */
    private void stopWinningTimer() {
        timer.cancel();
        timer = new Timer("winningTimer");
    }


//    public void magicianSetup() {
//        isStarted = true;
//
//        String nickname = game.getActivePlayers().get(Magician.values().length - game.getAvailableMagicians().size()).getNickname();
//        ReqMagicianMessage req = new ReqMagicianMessage("Please choose your magician", game.getAvailableMagicians());
//
//        server.getClientByID(server.getIDByNickname(nickname)).send(req);
//        sendAllExcept(new CustomMessage("User " + nickname + " is choosing his magician!"), server.getIDByNickname(nickname));
//    }

    /**
     * Method performAction calls the homonym method on the controller and executes a given action on the controller.
     *
     * @param action of type Performable. The action that needs to be executed.
     * @throws InvalidPlayerException if the action's player is not in the current game.
     * @throws RoundOwnerException    if the action's player is not the current round owner.
     * @throws GameException          if there is no round owner, or you are the only player left in the game (the game is frozen).
     */
    public void performAction(Performable action) throws InvalidPlayerException, RoundOwnerException, GameException {
        controller.performAction(action);
    }
}