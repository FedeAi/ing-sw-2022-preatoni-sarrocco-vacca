package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Server.Answer.game.HandMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * MoveMotherListener class is a WorkerListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class HandListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public HandListener(VirtualClient client) {
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
        HandMessage message = new HandMessage((List<AssistantCard>) evt.getNewValue());
        virtualClient.send(message);    
    }
}