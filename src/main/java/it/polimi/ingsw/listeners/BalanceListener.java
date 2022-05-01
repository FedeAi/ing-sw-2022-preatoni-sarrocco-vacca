package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Server.Answer.BalanceMessage;
import it.polimi.ingsw.Server.Answer.CloudsMessage;
import it.polimi.ingsw.Server.VirtualView;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * MoveMotherListener class is a WorkerListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class BalanceListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public BalanceListener(VirtualView client) {
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
        BalanceMessage message = new BalanceMessage((int) evt.getNewValue());
        virtualView.send(message);
    }
}