package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Server.Answer.modelUpdate.MagicianMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

/**
 * MagicianListener class is a AbsListener used for notifying the client after magician choose.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class MagicianListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public MagicianListener(VirtualClient client, String propertyName) {
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
        MagicianMessage message = new MagicianMessage((Map<Magician,String>) evt.getNewValue());
        virtualClient.send(message);
    }
}