package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Server.answers.model.MagicianMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.Map;

/**
 * MagicianListener class is a AbsListener used for notifying the client after a magician selection.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */
public class MagicianListener extends AbsListener {

    /**
     * Constructor MagicianListener creates a new MagicianListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public MagicianListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a MagicianMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MagicianMessage message = new MagicianMessage((Map<Magician, String>) evt.getNewValue());
        virtualClient.send(message);
    }
}