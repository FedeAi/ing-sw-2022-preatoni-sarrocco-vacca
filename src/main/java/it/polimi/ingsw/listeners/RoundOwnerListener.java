package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.RoundOwnerMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * RoundOwnerListener class is a AbsListener used for notifying the client after a round owner change.
 *
 * @author Federico Sarrocco
 * @see AbsListener
 */
public class RoundOwnerListener extends AbsListener {

    /**
     * Constructor RoundOwnerListener creates a new RoundOwnerListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public RoundOwnerListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a RoundOwnerMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RoundOwnerMessage message = new RoundOwnerMessage((String) evt.getNewValue());
        virtualClient.send(message);
    }
}