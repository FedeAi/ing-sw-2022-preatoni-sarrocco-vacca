package it.polimi.ingsw.Server.answers;

/**
 * Class PingMessage is a type of Action used for sending a ping to a client.
 */
public class PingMessage implements Answer {

    public PingMessage() {}

    /**
     * Method getMessage returns a new Object.
     */
    @Override
    public Object getMessage() {
        return new Object();
    }
}
