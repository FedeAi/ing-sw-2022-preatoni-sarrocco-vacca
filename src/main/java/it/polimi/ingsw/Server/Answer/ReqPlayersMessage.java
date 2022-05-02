package it.polimi.ingsw.Server.Answer;

public class ReqPlayersMessage implements Answer {
    private String message;

    public ReqPlayersMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
