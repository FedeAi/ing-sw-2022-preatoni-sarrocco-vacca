package it.polimi.ingsw.Server.Answer;

public class PingMessage implements Answer {

    public PingMessage() {}

    @Override
    public Object getMessage() {
        return new Object();
    }

}
