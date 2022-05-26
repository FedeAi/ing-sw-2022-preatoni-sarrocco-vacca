package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Actions.ChooseCloud;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Actions.PlayCard;
import it.polimi.ingsw.Controller.Rules.WinController;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
        updateRoundOwnerAndGameState(action);

        String winner = WinController.check(gameInstance);
        if (winner != null) {
            gameManager.getGameHandler().sendVictory(winner);
        }

    }

    private void updateRoundOwnerAndGameState(Performable action){
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
            gameManager.getGameHandler().reEnterWaitingPlayers();
            nextPlayer = gameInstance.setPlanningOrder();
        }

        // if next round is going to start
        if(gameInstance.getGameState() == GameState.ACTION_CHOOSE_CLOUD && nextState == GameState.PLANNING_CHOOSE_CARD){
            // deactivate Character effects if there is an active card TODO not here
            IntStream.range(0, gameInstance.getCharacterCards().size()).filter(i -> gameInstance.getCharacterCards().get(i).isActive()).forEach(i->gameInstance.deactivateCharacterCard(i,gameManager.getRules()));
        }

        gameInstance.setRoundOwner(nextPlayer);
        gameInstance.setGameState(nextState);
    }

    public void handleNewRoundOwnerOnDisconnect(String nickname) {
        if(nickname.equals(gameInstance.getRoundOwner().getNickname()) && gameInstance.numActivePlayers()> 1){
            switch (gameInstance.getGameState()){
                case PLANNING_CHOOSE_CARD -> updateRoundOwnerAndGameState(new PlayCard(nickname, -1));
                default -> updateRoundOwnerAndGameState(new ChooseCloud(nickname, -1));
            }
        }
    }




}
