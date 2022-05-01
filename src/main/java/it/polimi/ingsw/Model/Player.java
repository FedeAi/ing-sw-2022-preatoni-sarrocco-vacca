package it.polimi.ingsw.Model;

import it.polimi.ingsw.Constants.Pair;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.Magician;
import it.polimi.ingsw.Server.VirtualView;
import it.polimi.ingsw.listeners.BalanceListener;
import it.polimi.ingsw.listeners.IslandsListener;
import it.polimi.ingsw.listeners.MoveMotherListener;
import it.polimi.ingsw.listeners.SchoolListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO remove playedCard at the end of the turn
public class Player implements PropertyChangeListener {
    public static final String HAND_LISTENER = "handListener";
    public static final String SCHOOL_LISTENER = "schoolListener";
    public static final String BALANCE_LISTENER = "balanceListener";

    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private AssistantCard playedCard; // last played card
    private Magician magician;
    private int balance;

    protected final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public Player(String nickname) { // FIXME: number numPlayers, higher order data, not relatives to Player
        this.nickname = nickname;
        connected = true;
        createHand();
    }

    /**
     * Method createListeners creates the Map of listeners.
     * @param client virtualClient - the VirtualClient on the server.
     */
    public void createListeners(VirtualView client){
        listeners.addPropertyChangeListener(HAND_LISTENER, new MoveMotherListener(client));
        listeners.addPropertyChangeListener(SCHOOL_LISTENER, new SchoolListener(client));   // TODO ricordarsi di fare sendall
        listeners.addPropertyChangeListener(BALANCE_LISTENER, new BalanceListener(client));
    }


    public void initBalance(int balance) {
        this.balance = balance;
    }

    public void setSchool(School school) {
        this.school = school;
        school.addPlayerListener(this);
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

    public void setMagician(Magician magician) {
        this.magician = magician;
    }

    public Magician getMagician() {
        return magician;
    }

    public void addCoin() {
        int oldBalance = balance;
        balance++;
        listeners.firePropertyChange(BALANCE_LISTENER, oldBalance, balance);
    }

    public void spendCoins(int amount) {
        int oldBalance = balance;
        balance -= amount;
        balance = Math.max(balance, 0);
        listeners.firePropertyChange(BALANCE_LISTENER, oldBalance, balance);
    }

    public int getBalance() {
        return balance;
    }

    // empty string in AC constructor, this needs to be sorted
    private void createHand() {
        cards = new ArrayList<>();
        for (int i = 1; i <= Rules.numAssistantCards; i++) {
            cards.add(new AssistantCard("", i));
        }
        listeners.firePropertyChange(HAND_LISTENER, null, cards);
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
    public void setAndRemovePlayedCard(AssistantCard playedCard) {  // TODO make protect
        ArrayList<AssistantCard> oldCards = new ArrayList<>(cards);
        this.playedCard = playedCard;
        cards.remove(playedCard);
        listeners.firePropertyChange(HAND_LISTENER, oldCards, cards);
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

    // TODO FIXME THIS IS NOT bello, il propertyChange è triggerato dalla scuola
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        listeners.firePropertyChange(SCHOOL_LISTENER, null, new Pair<String,School>(this.nickname, this.school));
    }
}
