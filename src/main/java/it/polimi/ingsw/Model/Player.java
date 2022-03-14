package it.polimi.ingsw.Model;

import java.util.List;

public class Player {
    String nickname;
    boolean connected;
    School school;
    List<AssistantCard> cards;
    Magician magician;

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
