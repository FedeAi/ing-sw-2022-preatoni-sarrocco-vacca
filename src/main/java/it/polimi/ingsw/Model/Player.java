package it.polimi.ingsw.Model;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Server.VirtualClient;
import it.polimi.ingsw.listeners.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Player class represents the user and player of the board game.
 */
public class Player implements PropertyChangeListener {

    public static final String HAND_LISTENER = "handListener";
    public static final String SCHOOL_LISTENER = "schoolListener";
    public static final String BALANCE_LISTENER = "balanceListener";
    public static final String PLAYED_CARD_LISTENER = "playedCardListener";

    private final int playerID;
    private String nickname;
    private boolean connected;
    private School school;
    private List<AssistantCard> cards;
    private AssistantCard playedCard; // last played card
    private Magician magician;
    private int balance;

    protected final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private final HashMap<VirtualClient, List<AbsListener>> clientMapLister = new HashMap<VirtualClient, List<AbsListener>>();

    /**
     * Constructor Player creates a Player instance, and it initializes the Player's hand.
     *
     * @param nickname the selected nickname.
     * @param playerID the user unique id.
     * @see Player#createHand
     */
    public Player(int playerID, String nickname) {
        this.playerID = playerID;
        this.nickname = nickname;
        connected = true;
        createHand();
    }

    /**
     * Method createListeners creates the Map of listeners.
     *
     * @param client the VirtualClient on the server.
     */
    public void createListeners(VirtualClient client) {
        ArrayList<AbsListener> createdListeners = new ArrayList<>();

        createdListeners.add(0, new HandListener(client, HAND_LISTENER));
        listeners.addPropertyChangeListener(HAND_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new SchoolListener(client, SCHOOL_LISTENER));
        listeners.addPropertyChangeListener(SCHOOL_LISTENER, createdListeners.get(0));   // TODO ricordarsi di fare sendall

        createdListeners.add(0, new BalanceListener(client, BALANCE_LISTENER));
        listeners.addPropertyChangeListener(BALANCE_LISTENER, createdListeners.get(0));

        createdListeners.add(0, new PlayedCardListener(client, PLAYED_CARD_LISTENER));
        listeners.addPropertyChangeListener(PLAYED_CARD_LISTENER, createdListeners.get(0));

        clientMapLister.put(client, createdListeners);
    }

    /**
     * Method removeListeners removes the client's listeners from the Model,
     * and then it removes the client from the Map.
     *
     * @param client the VirtualClient on the server.
     */
    public void removeListeners(VirtualClient client) {
        if (clientMapLister.containsKey(client)) {
            for (AbsListener listener : clientMapLister.get(client)) {
                listeners.removePropertyChangeListener(listener.getPropertyName(), listener);
            }
        }
    }

    /**
     * Method fireInitialState fires the Player's Model data to be sent to the client.
     */
    public void fireInitialState() {
        listeners.firePropertyChange(HAND_LISTENER, null, cards);
        listeners.firePropertyChange(SCHOOL_LISTENER, null, school);
        listeners.firePropertyChange(BALANCE_LISTENER, null, balance);
    }

    /**
     * Method initBalance initializes the initial player balance.
     *
     * @param balance the balance to be set
     */
    public void initBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Method setSchool is a method for setting the Player's School.
     *
     * @param school the School to be set.
     * @see School
     */
    public void setSchool(School school) {
        this.school = school;
        school.addPlayerListener(this);
    }

    /**
     * Method getNickname returns the Player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method getNickname returns the Player's ID.
     */
    public int getID() {
        return playerID;
    }

    /**
     * Method isConnected returns if a Player is connected.
     *
     * @return true if then player is connected, false otherwise.
     */
    public synchronized boolean isConnected() {
        return connected;
    }

    /**
     * Method setConnected sets the connection status of a player.
     *
     * @param connected the status flag for the Player's connection.
     */
    public synchronized void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Method getSchool returns the School's reference.
     */
    public School getSchool() {
        return school;
    }

    /**
     * Method getCards returns the Player's hand.
     */
    public List<AssistantCard> getCards() {
        return cards;
    }

    /**
     * Method getMagician returns the Player's magician.
     */
    public Magician getMagician() {
        return magician;
    }

    /**
     * Method setMagician sets the Player's magician.
     */
    public void setMagician(Magician magician) {
        this.magician = magician;
        //TODO add listener
    }

    /**
     * Method addCoin adds a coin to the Player's balance.
     */
    public void addCoin() {
        int oldBalance = balance;
        balance++;
        listeners.firePropertyChange(BALANCE_LISTENER, oldBalance, balance);
    }

    /**
     * Method spendCoins removes coins from the Player's balance.
     *
     * @param amount the amount that has been spent.
     */
    public void spendCoins(int amount) {
        int oldBalance = balance;
        balance -= amount;
        balance = Math.max(balance, 0);
        listeners.firePropertyChange(BALANCE_LISTENER, oldBalance, balance);
    }

    /**
     * Method getBalance returns the current Player's balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Method createHand is responsible for the initialization of a Player's cards.
     * It creates the initial 10 AssistantCards.
     *
     * @see AssistantCard
     */
    private void createHand() {
        cards = new ArrayList<>();
        for (int i = 1; i <= Constants.NUM_ASSISTANT_CARDS; i++) {
            cards.add(new AssistantCard("", i));
        }
        listeners.firePropertyChange(HAND_LISTENER, null, cards);
    }

    /**
     * Method hasCard checks if a player has a certain AssistantCard.
     *
     * @param card the value of the AssistantCard to check the presence for.
     * @return true if the card is present in the Player's hand, false otherwise.
     */
    public boolean hasCard(int card) {
        return cards.stream().anyMatch(c -> c.getValue() == card);
    }

    /**
     * Method getPlayedCard returns the Player's last played card.
     *
     * @return
     */
    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    /**
     * Method setAndRemovePlayedCard sets the played card and removes it from the Player's hand.
     *
     * @param playedCard the selected card to be played.
     */
    public void setAndRemovePlayedCard(AssistantCard playedCard) {  // TODO make protect
        ArrayList<AssistantCard> oldCards = new ArrayList<>(cards);
        this.playedCard = playedCard;
        cards.remove(playedCard);
        listeners.firePropertyChange(HAND_LISTENER, oldCards, cards);
        listeners.firePropertyChange(PLAYED_CARD_LISTENER, null, playedCard);
    }

    /**
     * Method removePlayedCard resets the Player's playedCard to null.
     */
    protected void removePlayedCard() {
        listeners.firePropertyChange(PLAYED_CARD_LISTENER, playedCard, null);
        this.playedCard = null;
    }

    /**
     * Method equals overrides the default behaviour of the Equals method,
     * in order to compare different Player instances.
     *
     * @param o the Player to compare
     * @return true if the nicknames correspond, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }

    /**
     * Method hashCode overrides the default behaviour of the hashCode method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    // TODO FIXME THIS IS NOT bello, il propertyChange è triggerato dalla scuola

    /**
     * Method propertyChange forwards the School's updates to be sent to the Client.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        listeners.firePropertyChange(SCHOOL_LISTENER, null, school);
    }
}
