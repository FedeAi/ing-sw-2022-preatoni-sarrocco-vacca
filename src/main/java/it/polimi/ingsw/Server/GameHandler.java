package it.polimi.ingsw.Server;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.*;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.Answer.*;

import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * GameHandler class handles a single match, instantiating a game mode (Game class) and a main controller (Controller
 * class). It also manages the startup phase, like the marker's color selection.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 */
public class GameHandler {

    private static final String PLAYER = "Player";
    private final Server server;
    private final GameManager controller;
    private final Game game;
    private final PropertyChangeSupport controllerListener = new PropertyChangeSupport(this);
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Random rnd = new Random();
    private boolean isStarted;
    private boolean isEnded;
    private int playersNumber;
    Timer timer = new Timer("WinningTimer"); // to decree victory in case of 1 player remaining

    /**
     * Constructor GameHandler creates a new GameHandler instance.
     *
     * @param server of type Server - the main server class.
     */
    public GameHandler(Server server) {
        this.server = server;
        isStarted = false;
        isEnded = false;
        game = new Game();
        controller = new GameManager(game, this);   // new GameManager(game, this);
        controllerListener.addPropertyChangeListener(controller);
    }

    public void createPlayer(String nickName, int player_id) {
        game.createPlayer(player_id, nickName);
    }

    public void reEnterPlayer(String nickName) {
        game.addPlayerToBeReconnected(nickName);
        game.createListeners(server.getClientByID(server.getIDByNickname(nickName)));
        game.fireInitalState();
        if(game.numActivePlayers()==1){
            reEnterWaitingPlayers();
        }
    }

    public void reEnterWaitingPlayers(){
        stopWinningTimer();
        game.getWaitingPlayersReconnected().forEach(p -> sendAll(new CustomMessage(p + " is back in the play")));
        game.reEnterWaitingPlayers();
    }

    public void sendAll(Answer message) {
        for (Player p : game.getPlayers()) {
            server.getClientByID(p.getID()).send(message);
        }
    }

    public void sendAllExcept(Answer message, int excludedID) {
        for (Player p : game.getActivePlayers()) {
            if (server.getIDByNickname(p.getNickname()) != excludedID) {
                server.getClientByID(p.getID()).send(message);
            }
        }
    }

    public void setPlayersNumber(int number) {
        playersNumber = number;
    }

    public void setExportMode(boolean expert) {
        game.setExpertMode(expert);
    }

    public boolean isStarted() {
        return isStarted;
    }

    public synchronized boolean isEnded() {
        return isEnded;
    }

    public boolean isSetupPhase() {
        return GameState.SETUP_CHOOSE_MAGICIAN.equals(game.getGameState());
    }

    public synchronized void setEnded() {
        isEnded = true;
    }

    public void startGame() throws InterruptedException{
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

    public void endGame() {
        setEnded();
        while (!game.getActivePlayers().isEmpty()) {
            server.getClientByID(game.getActivePlayers().get(0).getID()).getConnection().close();
        }
    }

    public void endGame(String leftNickname) {
        sendAll(new ConnectionMessage(PLAYER + " " + leftNickname + " left the game, the match will now end." +
                "\nThanks for playing!", false));
        while (!game.getActivePlayers().isEmpty()) {
            server.getClientByID(game.getActivePlayers().get(0).getID()).getConnection().close();
        }
    }

    public synchronized void unregisterPlayer(int id, boolean isGameEnded) {
        game.removeListeners(server.getClientByID(id));
        game.setPlayerConnected(id, false);
        if(game.getGameState()==GameState.SETUP_CHOOSE_MAGICIAN || isGameEnded) {
            endGame(server.getNicknameByID(id));
        }else{
            controller.handleNewRoundOwnerOnDisconnect(server.getNicknameByID(id));
            startWinningTimer();
        }

    }
    public void sendVictory(String player){
        sendAll(new WinMessage(player));
        endGame();
    }

    private void startWinningTimer() {
        if(game.numActivePlayers()== 1 && !isEnded()){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    sendAll( new CustomMessage("You WON, you are the only player and are passed "+ Constants.DELAY_WINNING_TIMER + " seconds"));
                    sendAll( new WinMessage(game.getPlayers().stream().filter(Player::isConnected).findFirst().get().getNickname()));
                    endGame();
                }
            };

            timer.schedule(timerTask, Constants.DELAY_WINNING_TIMER * 1000);
        }
    }

    private void stopWinningTimer(){
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

    public void performAction(Performable action) throws InvalidPlayerException, RoundOwnerException, GameException {
        controller.performAction(action);
    }
}
