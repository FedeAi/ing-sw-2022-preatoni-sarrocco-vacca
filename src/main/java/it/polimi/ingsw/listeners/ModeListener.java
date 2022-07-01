package it.polimi.ingsw.listeners;

import it.polimi.ingsw.server.answers.model.ModeMessage;
import it.polimi.ingsw.server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * ModeListener class is a AbsListener used for notifying the client after the game mode selection.
 *
 * @see AbsListener
 */
public class ModeListener extends AbsListener {

    /**
     * Constructor ModeListener creates a new ModeListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public ModeListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a ModeMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ModeMessage message = new ModeMessage((boolean) evt.getNewValue());
        virtualClient.send(message);
    }
}