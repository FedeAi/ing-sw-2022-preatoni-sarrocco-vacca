package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;

import java.util.Optional;

public interface DynamicRules {
    public abstract boolean influenceComparator(int influence1, int influence2);
    public abstract boolean checkProfessorInfluence(Game game, Color color);
    public abstract int computeMotherMaxMoves(AssistantCard card);
    /**
     * Computethe player with the maximum influence on that island, or Optional.Empty if no players have influence
     * @param game
     * @param islandIndex
     * @return
     */
    public abstract Optional<String> computeIslandInfluence(Game game, int islandIndex);
}
