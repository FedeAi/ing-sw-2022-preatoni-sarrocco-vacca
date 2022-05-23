package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.modelUpdate.SchoolMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class SchoolListener extends AbsListener {

    /**
     * Constructor MoveMotherListener creates a new MoveListener instance.
     *
     * @param client of type VirtualClient - the virtual client on the Server.
     */
    public SchoolListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a MoveMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SchoolMessage message = new SchoolMessage(virtualClient.getNickname(),(School) evt.getNewValue());
        virtualClient.sendAll(message);
    }
}