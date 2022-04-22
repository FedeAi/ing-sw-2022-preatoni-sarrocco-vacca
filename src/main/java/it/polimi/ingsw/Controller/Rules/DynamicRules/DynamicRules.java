package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;

import java.util.EnumMap;
import java.util.Optional;

public interface DynamicRules {
    /**
     * It checks if current round owner has influence on Color color.
     *
     * @param game
     * @return
     */
    public abstract EnumMap<Color, String> getProfessorInfluence(Game game);
    public abstract int computeMotherMaxMoves(AssistantCard card);
    /**
     * Computethe player with the maximum influence on that island, or Optional.Empty if no players have influence
     *
     * @param game
     * @param island
     * @return
     */
    public abstract Optional<String> computeIslandInfluence(Game game, Island island);

}
