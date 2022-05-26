package it.polimi.ingsw.Server.Answer;

public class ConnectionMessage implements Answer {
    private String message;
    private boolean validity;

    public ConnectionMessage(String message, boolean validity) {
        this.message = message;
        this.validity = validity;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return validity;
    }
}
