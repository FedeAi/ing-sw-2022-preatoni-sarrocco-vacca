package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.MoveMotherMessage;
import it.polimi.ingsw.Server.VirtualView;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a WorkerListener used for notifying the client after a move action.
 *
 * @author Alice Piemonti
 * @see WorkerListener
 */


public class AssistantCardListener extends AbsListener {

    /**
     * Constructor MoveMotherListener creates a new MoveListener instance.
     *
     * @param client of type VirtualClient - the virtual client on Server.
     */
    public AssistantCardListener(VirtualView client) {
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
        MoveMotherMessage message = new MoveMotherMessage((int) evt.getNewValue());
        virtualView.sendAll(message);
    }
}