package it.polimi.ingsw.model.cards;

import java.util.Objects;

/**
 * AssistantCard class represent the game's assistant cards, that determine the playing order of the game.
 */
public class AssistantCard implements Card {

    private int maxMoves;
    private int value;

    /**
     * Constructor AssistantCard sets the card values and the corresponding max moves.
     *
     * @param value the value of the card to create.
     */
    public AssistantCard(int value) {
        this.value = value;
        switch (this.value) {
            case 1, 2 -> maxMoves = 1;
            case 3, 4 -> maxMoves = 2;
            case 5, 6 -> maxMoves = 3;
            case 7, 8 -> maxMoves = 4;
            case 9, 10 -> maxMoves = 5;
        }
    }

    /**
     * Method getMaxMoves returns the card's max moves.
     */
    public int getMaxMoves() {
        return maxMoves;
    }

    /**
     * Methode getValue returns the card's value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Method equals overrides the default behaviour of the Equals method, in order to compare different AssistantCards
     *
     * @param o the card to compare
     * @return true if the value and maxMoves correspond, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return maxMoves == that.maxMoves && value == that.value;
    }

    /**
     * Method hashCode overrides the default behaviour of the hashCode method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(maxMoves, value);
    }

    /**
     * Method toString overrides the default behaviour of the toString method,
     * in order to provide a default description of the card.
     */
    @Override
    public String toString() {
        return "card " + getValue() + ", max moves: " + getMaxMoves();
    }
}