package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.server.answers.model.HandMessage;
import it.polimi.ingsw.server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * HandListener class is a AbsListener used for notifying the client after a player's hand update.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */
public class HandListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public HandListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a HandMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        HandMessage message = new HandMessage((ArrayList<AssistantCard>) evt.getNewValue());
        virtualClient.send(message);
    }
}