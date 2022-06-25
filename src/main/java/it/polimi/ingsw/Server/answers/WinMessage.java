package it.polimi.ingsw.Server.answers;

/**
 * Class ReqPlayersMessage is a type of Answer used for sending the game winner to all the players.
 */
public class WinMessage implements Answer {
    String message;

    /**
     * Constructor WinMessage creates a new WinMessage instance.
     *
     * @param message the message to be printed on the client's terminal.
     */
    public WinMessage(String message) {
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
