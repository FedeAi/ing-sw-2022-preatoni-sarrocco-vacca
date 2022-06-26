package it.polimi.ingsw.client.messages;

/**
 * SerializedMessage class represents the serialized version of Actions or Messages.
 */
public class SerializedMessage implements Message {

    public final Message message;
    public final Action action;

    /**
     * Constructor SerializedMessage creates a new SerializedMessage instance.
     *
     * @param message the Message to set to the SerializedMessage.
     */
    public SerializedMessage(Message message) {
        this.message = message;
        action = null;
    }

    /**
     * Constructor SerializedMessage creates a new SerializedMessage instance.
     *
     * @param action the Action to set to the SerializedMessage.
     */
    public SerializedMessage(Action action) {
        this.action = action;
        message = null;
    }
}