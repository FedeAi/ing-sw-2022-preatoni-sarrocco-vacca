package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Server.Answer.modelUpdate.BalanceMessage;
import it.polimi.ingsw.Server.Answer.modelUpdate.ModeMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class ModeListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public ModeListener(VirtualClient client) {
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
        ModeMessage message = new ModeMessage((boolean) evt.getNewValue());
        virtualClient.send(message);
    }
}