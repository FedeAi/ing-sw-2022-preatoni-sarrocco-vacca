package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.constants.Magician;

public class MagicianMessage implements Message{
    public final Magician magician;

    public MagicianMessage(Magician magician) {
        this.magician = magician;
    }
}
