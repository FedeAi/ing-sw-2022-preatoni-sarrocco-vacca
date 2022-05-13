package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.RoundOwnerMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * RoundOwnerListener class is a AbsListener used for notifying the client after a turn owner change.
 *
 * @author Federico Sarrocco
 * @see AbsListener
 */


public class RoundOwnerListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public RoundOwnerListener(VirtualClient client) {
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
        RoundOwnerMessage message = new RoundOwnerMessage((String) evt.getNewValue());
        virtualClient.send(message);
    }
}