package it.polimi.ingsw.Client.messages;

public class LoginMessage implements Message {
    private String nickname;

    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
