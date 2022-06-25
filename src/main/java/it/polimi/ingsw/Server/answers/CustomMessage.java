package it.polimi.ingsw.Server.answers;

/**
 * Class CustomMessage is a type of Answer used for sending generic messages.
 *
 * @see Answer
 */
public class CustomMessage implements Answer {

    String message;

    /**
     * Constructor CustomMessage creates the CustomMessage instance.
     *
     * @param message the message to be printed on the client's terminal.
     */
    public CustomMessage(String message) {
        this.message = message;
    }

    /**
     * Method getMessage returns the CustomMessage's message.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
