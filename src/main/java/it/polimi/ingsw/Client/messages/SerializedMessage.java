package it.polimi.ingsw.Client.messages;

public class SerializedMessage implements Message {
    public final Message message;
    public final Action action;

    public SerializedMessage(Message message) {
        this.message = message;
        action = null;
    }

    public SerializedMessage(Action action) {
        this.action = action;
        message = null;
    }
}
