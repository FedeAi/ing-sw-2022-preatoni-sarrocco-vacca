package it.polimi.ingsw.client.messages;

/**
 * LoginMessage class is a type of Message sent by the Client when it joins a game lobby.
 */
public class LoginMessage implements Message {

    private String nickname;

    /**
     * Constructor LoginMessage creates a new LoginMessage instance.
     *
     * @param nickname the player's selected name.
     */
    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method getNickname returns the selected nickname.
     *
     * @return The selected nickname.
     */
    public String getNickname() {
        return nickname;
    }
}