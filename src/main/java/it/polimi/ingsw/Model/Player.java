package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private AssistantCard playedCard; // last played card
    private Magician magician;

    public Player(String nickname, int number, int numPlayers, Magician magician) {
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
    private void createHand() {
        cards = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            cards.add(new AssistantCard("", i));
        }
    }

    public boolean hasCard(AssistantCard card) {
        return cards.contains(card);
    }

    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    /**
     * set played card and removes from available cards
     *
     * @param playedCard
     */
    public void setAndRemovePlayedCard(AssistantCard playedCard) {
        this.playedCard = playedCard;
        cards.remove(playedCard);
    }
}
