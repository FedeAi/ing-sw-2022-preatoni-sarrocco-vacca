package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.NextRoundMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * NextRoundListener class is a AbsListener used for notifying the client of a new round start.
 *
 * @author Federico Sarrocco
 * @see AbsListener
 */
public class NextRoundListener extends AbsListener {

    /**
     * Constructor NextRoundListener creates a new NextRoundListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public NextRoundListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a NextRoundMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        NextRoundMessage message = new NextRoundMessage();
        virtualClient.send(message);
    }
}