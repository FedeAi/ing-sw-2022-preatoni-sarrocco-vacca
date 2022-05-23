package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.ConnectedPlayersMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 *  ConnectedPlayerListener class is a AbsListener used for notifying the client after an update of the player list (disconnection or reconnection).
 *
 * @author Alessandro Vacca
 * @see AbsListener
 */

public class ConnectedPlayerListener extends AbsListener {

    /**
     * Constructor ConnectedPlayerListener creates a new ConnectedPlayerListener instance.
     *
     * @param client of type VirtualClient - the virtual client's view on Server.
     */
    public ConnectedPlayerListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a MoveMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ConnectedPlayersMessage message = new ConnectedPlayersMessage((List<String>) evt.getNewValue());
        virtualClient.send(message);
    }
}