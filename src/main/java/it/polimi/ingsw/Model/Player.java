package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.List;

public class Player {
    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private Magician magician;
/*
    public Player(String nickname){
        this.nickname = nickname;
        connected = true;
        manca school, cards, magician
    }
*/
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
