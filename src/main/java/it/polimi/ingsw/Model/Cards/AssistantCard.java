package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Constants.Animals;

import java.io.Serializable;
import java.util.Objects;

public class AssistantCard extends Card {

    private int maxMoves;
    private int value;

    // Every value has a corresponding maxMoves assigned.
    public AssistantCard(String imagePath, int value) {
        super(imagePath);
        this.value = value;

        switch (this.value) {
            case 1, 2 -> maxMoves = 1;
            case 3, 4 -> maxMoves = 2;
            case 5, 6 -> maxMoves = 3;
            case 7, 8 -> maxMoves = 4;
            case 9, 10 -> maxMoves = 5;
        }
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return maxMoves == that.maxMoves && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxMoves, value);
    }

    @Override
    public String toString() {
        return "card " + getValue() + ", max moves: " + getMaxMoves();
    }
}
