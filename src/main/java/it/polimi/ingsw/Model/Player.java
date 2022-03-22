package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private AssistantCard playedCard;
    private Magician magician;

    public Player(String nickname, int number, int numPlayers, Magician magician){
        this.nickname = nickname;
        connected = true;
        school = new School(numPlayers, number);
        createHand();
        this.magician = magician;
    }

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

    // empty string in AC constructor, this needs to be sorted
    private void createHand(){
        cards = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            cards.add(new AssistantCard("", i));
        }
    }
    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(AssistantCard playedCard) {
        this.playedCard = playedCard;
    }
}
