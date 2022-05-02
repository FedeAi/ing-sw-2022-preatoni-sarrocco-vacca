package it.polimi.ingsw.Client.messages;

public class SetupMessage implements Message {
    public final int playersNumber;
    public final boolean expertMode;

    public SetupMessage(int playersNumber, boolean expertMode) {
        this.playersNumber = playersNumber;
        this.expertMode = expertMode;
    }
}
