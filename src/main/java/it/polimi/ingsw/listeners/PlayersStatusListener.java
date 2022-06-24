package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.PlayersStatusMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

/**
 * PlayersStatusListener class is a AbsListener used for notifying the client after an update of the player list
 * (disconnection or reconnection).
 *
 * @author Alessandro Vacca
 * @see AbsListener
 */
public class PlayersStatusListener extends AbsListener {

    /**
     * Constructor PlayersStatusListener creates a new PlayersStatusListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public PlayersStatusListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a PlayersStatusMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayersStatusMessage message = new PlayersStatusMessage((Map<String, List<String>>) evt.getNewValue());
        virtualClient.send(message);
    }
}