package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.TowerColor;

public class Tower {
    private final TowerColor color;

    public Tower(TowerColor color) {
        this.color = color;
    }

    public TowerColor getColor() {
        return color;
    }
}