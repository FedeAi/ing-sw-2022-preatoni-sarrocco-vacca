package it.polimi.ingsw.listeners;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Server.Answer.modelUpdate.PlayedCardMessage;
import it.polimi.ingsw.Server.VirtualClient;

import java.beans.PropertyChangeEvent;

public class PlayedCardListener extends AbsListener {

    /**
     * Constructor PlayedCardListener creates a new PlayedCardListener instance.
     *
     * @param client of type VirtualView - the virtual client's view on the Server.
     */
    public PlayedCardListener(VirtualClient client) {
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
        PlayedCardMessage message = new PlayedCardMessage(virtualClient.getNickname(), (AssistantCard) evt.getNewValue());
        virtualClient.send(message);
    }
}
