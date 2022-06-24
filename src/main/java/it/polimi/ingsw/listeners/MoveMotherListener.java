package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.MotherMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after the change of MotherNature's position.
 *
 * @author Federico Sarrocco Alessandro Vacca
 * @see AbsListener
 */
public class MoveMotherListener extends AbsListener {

    /**
     * Constructor MoveMotherListener creates a new MoveMotherListener instance.
     *
     * @param client       the virtual client on the Server.
     * @param propertyName the type of the listener to be set.
     */
    public MoveMotherListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a MotherMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MotherMessage message = new MotherMessage((int) evt.getNewValue());
        virtualClient.send(message);
    }
}