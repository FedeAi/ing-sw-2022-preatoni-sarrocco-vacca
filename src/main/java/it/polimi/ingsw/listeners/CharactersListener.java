package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.server.answers.model.CharactersMessage;
import it.polimi.ingsw.server.VirtualClient;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * CharactersListener class is a AbsListener used for notifying the client after a change in character cards.
 *
 * @see AbsListener
 */
public class CharactersListener extends AbsListener {

    /**
     * Constructor CharactersListener creates a new CharactersListener instance.
     *
     * @param client       the virtual client's view on Server.
     * @param propertyName the type of the listener to be set.
     */
    public CharactersListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a CharactersMessage.
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