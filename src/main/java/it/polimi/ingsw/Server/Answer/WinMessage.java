package it.polimi.ingsw.Server.Answer;

public class WinMessage implements Answer {
    String message;

    public WinMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
