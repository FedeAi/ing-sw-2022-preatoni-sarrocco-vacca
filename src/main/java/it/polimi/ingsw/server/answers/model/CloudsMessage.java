package it.polimi.ingsw.server.answers.model;

import it.polimi.ingsw.model.Cloud;

import java.util.List;

/**
 * CloudsMessage class is type of ModelMessage used for sending updates of game's clouds.
 *
 * @see ModelMessage
 */
public class CloudsMessage implements ModelMessage {
    private final List<Cloud> message;

    /**
     * Constructor CloudsMessage creates a new MoveMessage instance.
     *
     * @param clouds the game's cloud list.
     */
    public CloudsMessage(List<Cloud> clouds) {
        this.message = clouds;
    }

    /**
     * Method getMessage returns the message of this Answer object.
     *
     * @return the message (type Object) of this Answer object.
     * @see ModelMessage#getMessage()
     */
    @Override
    public List<Cloud> getMessage() {
        return message;
    }
}