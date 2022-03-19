package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Animals;

public class AssistantCard extends Card{

    private int maxMoves;
    private int value;
    private Animals type;

    public AssistantCard(String imagePath, int value) {
        super(imagePath);
        this.value = value;

        switch(value) {
            case 1:
                maxMoves = 1;
            case 2:
                maxMoves = 1;
            case 3:
                maxMoves = 2;
            case 4:
                maxMoves = 2;
            case 5:
                maxMoves = 3;
            case 6:
                maxMoves = 3;
            case 7:
                maxMoves = 4;
            case 8:
                maxMoves = 4;
            case 9:
                maxMoves = 5;
            case 10:
                maxMoves = 5;
        }
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    public int getValue() {
        return value;
    }




}
