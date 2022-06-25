package it.polimi.ingsw.Server.answers;

/**
 * Class ReqPlayersMessage is a type of Answer used for requesting the lobby creation data
 * to the first client who connects.
 */
public class ReqPlayersMessage implements Answer {

    private String message;

    /**
     * Constructor ReqPlayersMessage creates a new ReqPlayersMessage instance.
     *
     * @param message the message to be printed on the client's terminal.
     */
    public ReqPlayersMessage(String message) {
        this.message = message;
    }

    /**
     * Method getMessage returns the ReqPlayersMessage's message.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
