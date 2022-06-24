package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.BalanceMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * BalanceListener class is a AbsListener used for notifying the client after a player's balance change.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */
public class BalanceListener extends AbsListener {

    /**
     * Constructor BalanceListener creates a new BalanceListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public BalanceListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a BalanceMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        BalanceMessage message = new BalanceMessage((int) evt.getNewValue());
        virtualClient.send(message);
    }
}