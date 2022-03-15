package it.polimi.ingsw.Model;

import java.util.List;

public class Player {
    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private Magician magician;

    public String getNickname() {
        return nickname;
    }

    public boolean isConnected() {
        return connected;
    }

    public School getSchool() {
        return school;
    }

    public List<AssistantCard> getCards() {
        return cards;
    }

    public Magician getMagician() {
        return magician;
    }
}
