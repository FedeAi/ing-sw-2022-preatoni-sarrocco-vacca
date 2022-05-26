package it.polimi.ingsw.Server.Answer;

public class CustomMessage implements Answer {
    String message;

    public CustomMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
