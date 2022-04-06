package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;

public interface DynamicRules {
    public boolean influenceComparator(int influence1, int influence2);
    public abstract boolean checkProfessorInfluence(Game game, Color color);
}
