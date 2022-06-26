package it.polimi.ingsw.client.messages;

/**
 * SetupMessage class represents the lobby creation type of Message.
 */
public class SetupMessage implements Message {

    public final int playersNumber;
    public final boolean expertMode;

    /**
     * Constructor SetupMessage creates a new SetupMessage instance.
     *
     * @param playersNumber the lobby creator selected player number.
     * @param expertMode    the lobby creator selected game mode (true if expert, false if normal).
     */
    public SetupMessage(int playersNumber, boolean expertMode) {
        this.playersNumber = playersNumber;
        this.expertMode = expertMode;
    }
}
