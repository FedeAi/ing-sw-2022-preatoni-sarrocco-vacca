package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Player {

    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private AssistantCard playedCard; // last played card
    private Magician magician;

    public Player(String nickname){ // FIXME: number numPlayers, higher order data, not relatives to Player
        this.nickname = nickname;
        connected = true;
        createHand();
    }

    public void setSchool(School school) {
        this.school = school;
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

    public void setMagician(Magician magician){
        this.magician = magician;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
