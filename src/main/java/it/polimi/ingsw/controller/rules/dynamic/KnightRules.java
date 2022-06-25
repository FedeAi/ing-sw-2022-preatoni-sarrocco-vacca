package it.polimi.ingsw.controller.rules.dynamic;

import java.util.Map;

/**
 * KnightRules class extends the BaseRules class,
 * overriding the rules that have changed since the activation of the Knight character card.
 */
public class KnightRules extends BaseRules {

    /**
     * Method turnOwnerInfluenceModifier adds 2 influence points to the player influence calculation.
     *
     * @param playerInfluence the map of all the player's influence on the island.
     * @param roundOwner      the nickname of the current round owner.
     */
    @Override
    protected void turnOwnerInfluenceModifier(Map<String, Integer> playerInfluence, String roundOwner) {
        super.turnOwnerInfluenceModifier(playerInfluence, roundOwner);
        playerInfluence.put(roundOwner, playerInfluence.getOrDefault(roundOwner, 0) + 2);
    }
}