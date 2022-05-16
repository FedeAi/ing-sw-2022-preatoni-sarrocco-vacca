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
import java.util.Comparator;
import java.util.List;

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

        if(nextPlayer == null && nextState == GameState.ACTION_MOVE_STUDENTS){
            nextPlayer = setActionOrder(gameInstance);
        }
        gameInstance.setGameState(nextState);
        gameInstance.setRoundOwner(nextPlayer);

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
    private Player setActionOrder(Game game) {
        List<Player> planningPhasePlayers = game.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() - p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);
        game.setPlayersActionPhase(planningPhasePlayers);
       return planningPhasePlayers.get(0);
    }

}
