package it.polimi.ingsw.server.answers;

/**
 * Class ConnectionMessage is a type of Answer used for player connection updates.
 *
 * @see Answer
 */
public class ConnectionMessage implements Answer {

    private String message;
    private boolean validity;

    /**
     * Constructor ConnectionMessage creates a new ConnectionMessage instance.
     *
     * @param message the message to be printed on the client's terminal.
     * @param validity the status of the player (connection/disconnection).
     */
    public ConnectionMessage(String message, boolean validity) {
        this.message = message;
        this.validity = validity;
    }

    /**
     * Method getMessage returns the ConnectionMessage's message.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Method isValid returns the ConnectionMessage's validity.
     */
    public boolean isValid() {
        return validity;
    }
}