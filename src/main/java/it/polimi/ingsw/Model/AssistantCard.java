package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Animals;

public class AssistantCard extends Card{

    private int maxMoves;
    private int value;
    private Animals type;

    public AssistantCard(String imagePath, int maxMoves) {
        super(imagePath);
        this.maxMoves = maxMoves;
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    public int getValue() {
        return value;
    }




}
