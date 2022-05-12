package it.polimi.ingsw.Client.messages;

/**
 * messaggio creazione lobby ( scelta magician successiva )
 */
public class SetupMessage implements Message {
    public final int playersNumber;
    public final boolean expertMode;

    public SetupMessage(int playersNumber, boolean expertMode) {
        this.playersNumber = playersNumber;
        this.expertMode = expertMode;
    }
}
