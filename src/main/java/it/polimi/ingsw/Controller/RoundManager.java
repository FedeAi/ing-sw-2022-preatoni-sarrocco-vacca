package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Actions.PlayCard;
import it.polimi.ingsw.Controller.Rules.WinController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.listeners.RoundOwnerListener;
import it.polimi.ingsw.Controller.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class RoundManager {

    public static final String ROUND_OWNER_LISTENER = "roundOwnerListener";


    private GameManager gameManager;
    private Game gameInstance;
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = gameManager.getGame();
        //listeners.addPropertyChangeListener(RoundOwnerListener());
    }

    public void addListener(PropertyChangeListener listener){
        listeners.addPropertyChangeListener(listener);
    }
    
    public void performAction(Performable action) throws InvalidPlayerException, RoundOwnerException, GameException {
        action.performMove(gameInstance, gameManager.getRules());
        /* this line is reached only if the action is performable  */

        GameState nextState = action.nextState(gameInstance, gameManager.getRules());
        Player nextPlayer = action.nextPlayer(gameInstance, gameManager.getRules());

        // Start the first planning phase
        if(nextPlayer == null && gameInstance.getGameState() == GameState.SETUP_CHOOSE_MAGICIAN){
            nextPlayer = gameInstance.setPlanningOrder();
        }

        if(nextPlayer == null && nextState == GameState.ACTION_MOVE_STUDENTS){
            nextPlayer = gameInstance.setActionOrder();
        }
        if(nextPlayer == null && nextState == GameState.PLANNING_CHOOSE_CARD){
            // handle the disconnected players now re-connected
            gameInstance.reEnterWaitingPlayers();
            nextPlayer = gameInstance.setPlanningOrder();
        }

        // if next round is going to start
        if(gameInstance.getGameState() == GameState.ACTION_CHOOSE_CLOUD && nextState == GameState.PLANNING_CHOOSE_CARD){
            // deactivate Character effects if there is an active card TODO not here
            IntStream.range(0, gameInstance.getCharacterCards().size()).filter(i -> gameInstance.getCharacterCards().get(i).isActive()).forEach(i->gameInstance.deactivateCharacterCard(i,gameManager.getRules()));
        }

        gameInstance.setRoundOwner(nextPlayer);
        gameInstance.setGameState(nextState);

        WinController.check(gameInstance);

    }

//
//
//    public nextPlayer handleNextPlayer(Performable action){
//        Player nextPlayer = action.nextPlayer(gameInstance, gameManager.getRules());
//        ret
//        if(nextPlayer==null){
//            // planning -> action : ordering players
//            if(action.nextState(gameInstance, gameManager.getRules()) == GameState.ACTION_MOVE_STUDENTS){
//                nextPlayer = setActionOrder(gameInstance);
//            }
//        }
//        gameInstance.setRoundOwner(nextPlayer);
//    }

    /**
     * choose Player orders for action phase
     */




}
