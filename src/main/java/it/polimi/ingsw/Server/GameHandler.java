package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.Answer.Answer;
import it.polimi.ingsw.Server.Answer.ConnectionMessage;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import it.polimi.ingsw.Server.Answer.ReqMagicianMessage;

import java.beans.PropertyChangeSupport;
import java.util.Random;
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
    private int started;
    private int playersNumber;

    /**
     * Constructor GameHandler creates a new GameHandler instance.
     *
     * @param server of type Server - the main server class.
     */
    public GameHandler(Server server) {
        this.server = server;
        started = 0;
        game = new Game();
        controller = new GameManager(game);   // new GameManager(game, this);
        controllerListener.addPropertyChangeListener(controller);
    }

    public void createPlayer(String nickName, int player_id) {
        game.createPlayer(player_id, nickName);
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

    public boolean isStarted() {
        return started > 0;
    }

    public void endGame() {
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

    public void unregisterPlayer(int id) {
        game.getPlayerByID(id).setConnected(false);
    }

    public void setup() {
        if (started == 0) started = 1;
        String nickname = game.getActivePlayers().get(playersNumber - game.getAvailableMagicians().size()).
                getNickname();
        ReqMagicianMessage req = new ReqMagicianMessage("Please choose your magician", game.getAvailableMagicians());

        server.getClientByID(server.getIDByNickname(nickname)).send(req);
        sendAllExcept(new CustomMessage("User " + nickname + " is choosing his magician!"),
                server.getIDByNickname(nickname));
    }

}
