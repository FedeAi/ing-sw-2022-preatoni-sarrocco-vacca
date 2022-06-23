package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;

import java.util.EnumMap;
import java.util.Optional;

/**
 * DynamicRules class is an interface to be implemented for each of the rule type.
 *
 * @see BaseRules
 */
public interface DynamicRules {

    /**
     * Method getProfessorInfluence checks if current round owner has the maximum influence on every color.
     *
     * @param game the current game instance
     * @return the updated map of professors - players.
     * @see FarmerRules
     */
    public abstract EnumMap<Color, String> getProfessorInfluence(Game game);

    /**
     * Method computeMotherMaxMoves returns the value of the maximum movement of MotherNature.
     *
     * @param card the AssistantCard that has been played by the current player.
     * @see PostmanRules
     */
    public abstract int computeMotherMaxMoves(AssistantCard card);

    /**
     * Method computeIslandInfluence calculates the player with the maximum influence on that island,
     * or Optional.Empty if no players have influence.
     *
     * @param game   the current game instance reference.
     * @param island the reference to the island to calculate the influence on.
     * @return The nickname of the new owner, empty otherwise.
     * @see KnightRules
     * @see CentaurRules
     */
    public abstract Optional<String> computeIslandInfluence(Game game, Island island);
}