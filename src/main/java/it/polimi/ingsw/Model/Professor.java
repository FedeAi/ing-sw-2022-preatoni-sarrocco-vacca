package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

public class Professor {
    private Color color;

    public Professor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
