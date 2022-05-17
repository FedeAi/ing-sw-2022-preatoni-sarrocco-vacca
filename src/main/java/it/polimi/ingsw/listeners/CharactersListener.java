package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Server.Answer.modelUpdate.CharactersMessage;
import it.polimi.ingsw.Server.Answer.modelUpdate.HandMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * MoveMotherListener class is a AbsListener used for notifying the client after a move action.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see AbsListener
 */


public class CharactersListener extends AbsListener {

    /**
     * Constructor HandListener creates a new HandListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on Server.
     */
    public CharactersListener(VirtualClient client) {
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
        CharactersMessage message = new CharactersMessage((ArrayList<CharacterCard>) evt.getNewValue());
        virtualClient.send(message);    
    }
}