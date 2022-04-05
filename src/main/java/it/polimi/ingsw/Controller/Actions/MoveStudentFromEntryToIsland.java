package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;

public class MoveStudentFromEntryToIsland extends MoveStudentFromEntry{
    MoveStudentFromEntryToIsland(String player, Color color) {
        super(player, color);
    }

    @Override
    public void performMove(Game game) {

    }
}
