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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;

public class RoundManager implements PropertyChangeListener {

    private GameManager gameManager;
    private Game gameInstance;

    public RoundManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameInstance = gameManager.getGame();
    }

    
    public void performAction(Performable action) throws InvalidPlayerException, RoundOwnerException, GameException {
        action.performMove(gameInstance, gameManager.getRules());
        /* this line is reached only if the action is performable  */

        handleTurnChange(action);
        WinController.check(gameInstance);

    }


    public void handleTurnChange(Performable action){
        GameState nextState = action.nextState(gameInstance, gameManager.getRules());
        gameInstance.setGameState(nextState);

        // phase change from planning to action
        if(action instanceof PlayCard &&  nextState == GameState.ACTION_MOVE_STUDENTS){
            setActionOrder(gameInstance);
        }

    }

    public void handleNextPlayer()

    /**
     * choose Player orders for action phase
     */
    private void setActionOrder(Game game) {
        List<Player> planningPhasePlayers = game.getOrderedPlanningPlayers();

        // compare by cards value
        Comparator<Player> compareByCardValue = (Player p1, Player p2) -> p1.getPlayedCard().getValue() - p2.getPlayedCard().getValue();
        planningPhasePlayers.sort(compareByCardValue);
        game.setPlayersActionPhase(planningPhasePlayers);
        game.setRoundOwner(planningPhasePlayers.get(0));

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
