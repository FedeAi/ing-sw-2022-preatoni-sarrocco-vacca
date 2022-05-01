package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Server.Answer.BalanceMessage;
import it.polimi.ingsw.Server.Answer.ProfsMessage;
import it.polimi.ingsw.Server.VirtualView;

import java.beans.PropertyChangeEvent;
import java.util.EnumMap;

/**
 * MoveMotherListener class is a WorkerListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class ProfsListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public ProfsListener(VirtualView client) {
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
        ProfsMessage message = new ProfsMessage((EnumMap<Color, String>) evt.getNewValue());
        virtualView.send(message);
    }
}