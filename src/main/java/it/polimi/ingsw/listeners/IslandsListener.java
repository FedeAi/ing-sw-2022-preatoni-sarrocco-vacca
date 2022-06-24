package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Server.Answer.modelUpdate.IslandsMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after an island update.
 *
 * @see AbsListener
 */
public class IslandsListener extends AbsListener {

    /**
     * Constructor IslandsListener creates a new IslandsListener instance.
     *
     * @param client the virtual client on Server.
     * @param propertyName the type of the listener to be set.
     */
    public IslandsListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with an IslandsMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        IslandsMessage message = new IslandsMessage((IslandContainer) evt.getNewValue());
        virtualClient.send(message);
    }
}