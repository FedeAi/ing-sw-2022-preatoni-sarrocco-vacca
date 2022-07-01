package it.polimi.ingsw.listeners;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.server.answers.model.ProfsMessage;
import it.polimi.ingsw.server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.EnumMap;

/**
 * ProfsListener class is a AbsListener used for notifying the client after change in professor ownership.
 *
 * @see AbsListener
 */
public class ProfsListener extends AbsListener {

    /**
     * Constructor ProfsListener creates a new ProfsListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public ProfsListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a ProfsMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ProfsMessage message = new ProfsMessage((EnumMap<Color, String>) evt.getNewValue());
        virtualClient.send(message);
    }
}