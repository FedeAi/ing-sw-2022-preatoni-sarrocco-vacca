package it.polimi.ingsw.controller;

import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.actions.ChooseCloud;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.actions.PlayCard;
import it.polimi.ingsw.controller.rules.WinController;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Method RoundManager is the Controller class that handles game states and turn logic.
 */
public class RoundManager {

    public static final String ROUND_OWNER_LISTENER = "roundOwnerListener";

    private GameManager gameManager;
    private Game gameInstance;
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    /**
     * Constructor RoundManager creates a new RoundManager instance.
     *
     * @param gameManager the parent Controller that handles the Game.
     * @see GameManager
     */
    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = gameManager.getGame();
        //listeners.addPropertyChangeListener(RoundOwnerListener());
    }

    /**
     * Method addListener adds a listener to trigger the GameManager.
     *
     * @param listener the listener to be added.
     */
    public void addListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    /**
     * Method performAction executes the Performable instance on the Game model, if possibile.
     * Only if an action is successful, the game state and roundOwner are updated.
     * It also checks if a Player has won.
     *
     * @param action the Performable to be executed on the Game Model.
     * @throws InvalidPlayerException if the action's player is not valid.
     * @throws RoundOwnerException    if the action's player is not the current round owner.
     * @throws GameException          if a generic error is thrown.
     * @see Performable
     * @see WinController
     */
    public void performAction(Performable action) throws InvalidPlayerException, RoundOwnerException, GameException {
        action.performMove(gameInstance, gameManager.getRules());
        /* this line is reached only if the action is performable  */
        updateRoundOwnerAndGameState(action);

        String winner = WinController.check(gameInstance);
        if (winner != null) {
            gameManager.getGameHandler().sendVictory(winner);
        }
    }

    /**
     * Method updateRoundOwnerAndGameState handles the selection of the next game state and round owner
     * based on the action's output.
     *
     * @param action the Performable that provides the next state and owner.
     */
    private void updateRoundOwnerAndGameState(Performable action) {
        GameState nextState = action.nextState(gameInstance, gameManager.getRules());
        Player nextPlayer = action.nextPlayer(gameInstance, gameManager.getRules());
        // Start the first planning phase
        if (nextPlayer == null && gameInstance.getGameState() == GameState.SETUP_CHOOSE_MAGICIAN) {
            nextPlayer = gameInstance.setPlanningOrder();
        }
        // starting a new action phase
        if (nextPlayer == null && nextState == GameState.ACTION_MOVE_STUDENTS) {
            nextPlayer = gameInstance.setActionOrder();
        }
        // starting a new planning phase
        if (nextPlayer == null && nextState == GameState.PLANNING_CHOOSE_CARD) {
            // remove played cards to all players
            gameInstance.removePlayedCards();
            // handle the disconnected players now re-connected
            gameManager.getGameHandler().reEnterWaitingPlayers();
            nextPlayer = gameInstance.setPlanningOrder();
        }
        // if next round is going to start
        if (gameInstance.getGameState() == GameState.ACTION_CHOOSE_CLOUD && nextState == GameState.PLANNING_CHOOSE_CARD) {
            // deactivate Character effects if there is an active card TODO not here
            IntStream.range(0, gameInstance.getCharacterCards().size()).filter(i -> gameInstance.getCharacterCards().get(i).isActive()).forEach(i -> gameInstance.deactivateCharacterCard(i, gameManager.getRules()));
        }
        gameInstance.setRoundOwner(nextPlayer);
        gameInstance.setGameState(nextState);
    }

    /**
     * Method handleNewRoundOwnerOnDisconnect manages the disconnection of a Player while he is the current round owner.
     *
     * @param nickname the nickname of the disconnected Player.
     */
    public void handleNewRoundOwnerOnDisconnect(String nickname) {
        if (Objects.equals(nickname, gameInstance.getRoundOwner().getNickname()) && gameInstance.numActivePlayers() > 1) {
            switch (gameInstance.getGameState()) {
                case PLANNING_CHOOSE_CARD -> updateRoundOwnerAndGameState(new PlayCard(nickname, -1));
                default -> updateRoundOwnerAndGameState(new ChooseCloud(nickname, -1));
            }
        }
    }
}