package it.polimi.ingsw.Client.messages;

import it.polimi.ingsw.Constants.Magician;

public class MagicianMessage implements Message{
    public final Magician magician;

    public MagicianMessage(Magician magician) {
        this.magician = magician;
    }
}
