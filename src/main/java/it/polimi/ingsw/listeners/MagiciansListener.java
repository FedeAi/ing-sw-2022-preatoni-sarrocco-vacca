package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Server.Answer.game.MagiciansMessageDeprecated;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * MagicianListener class is a WorkerListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class MagiciansListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public MagiciansListener(VirtualClient client) {
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
        MagiciansMessageDeprecated message = new MagiciansMessageDeprecated((List<Magician>) evt.getNewValue());
        virtualClient.sendAll(message);
    }
}