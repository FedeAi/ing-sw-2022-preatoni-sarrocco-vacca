package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

public class Student {
    private Color color;

    public Student(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}