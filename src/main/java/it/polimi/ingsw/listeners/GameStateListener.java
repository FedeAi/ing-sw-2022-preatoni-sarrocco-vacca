package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Server.Answer.modelUpdate.BalanceMessage;
import it.polimi.ingsw.Server.Answer.modelUpdate.GameStateMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Davide Preatoni
 * @see AbsListener
 */


public class GameStateListener extends AbsListener {

    /**
     * Constructor GameStateListener creates a new GameStateListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public GameStateListener(VirtualClient client) {
        super(client);
    }

    /**
     * Method propertyChange notifies the client with a MoveMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameStateMessage message = new GameStateMessage((GameState) evt.getNewValue());
        virtualClient.send(message);
    }
}