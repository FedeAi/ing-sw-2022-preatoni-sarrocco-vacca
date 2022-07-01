package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.server.answers.model.PlayedCardMessage;
import it.polimi.ingsw.server.VirtualClient;

import java.beans.PropertyChangeEvent;

/**
 * PlayedCardListener class is a AbsListener used for notifying the client after a PlayCard action.
 *
 * @see AbsListener
 */
public class PlayedCardListener extends AbsListener {

    /**
     * Constructor PlayedCardListener creates a new PlayedCardListener instance.
     *
     * @param client       the virtual client's view on the Server.
     * @param propertyName the type of the listener to be set.
     */
    public PlayedCardListener(VirtualClient client, String propertyName) {
        super(client, propertyName);
    }

    /**
     * Method propertyChange notifies the client with a PlayedCardMessage.
     *
     * @param evt of type PropertyChangeEvent - the event received.
     * @see AbsListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayedCardMessage message = new PlayedCardMessage(virtualClient.getNickname(), (AssistantCard) evt.getNewValue());
        virtualClient.sendAll(message);
    }
}