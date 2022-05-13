package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Server.Answer.modelUpdate.IslandsMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after a move action.
 *
 * @see AbsListener
 */


public class IslandsListener extends AbsListener {

    /**
     * Constructor MoveMotherListener creates a new MoveListener instance.
     *
     * @param client of type VirtualClient - the virtual client on Server.
     */
    public IslandsListener(VirtualClient client) {
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
        IslandsMessage message = new IslandsMessage((IslandContainer) evt.getNewValue());
        virtualClient.sendAll(message);
    }
}