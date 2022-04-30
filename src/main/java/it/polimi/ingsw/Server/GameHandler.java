package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;

import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.logging.Logger;

/**
 * GameHandler class handles a single match, instantiating a game mode (Game class) and a main controller (Controller
 * class). It also manages the startup phase, like the marker's color selection.
 * @author GC30

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

}
