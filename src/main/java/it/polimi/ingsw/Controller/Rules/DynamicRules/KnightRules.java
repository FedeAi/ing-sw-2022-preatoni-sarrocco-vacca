package it.polimi.ingsw.Controller.Rules.DynamicRules;

import java.util.Map;

/**
 * Activating the related effect it adds 2 influence points to the player
 */
public class KnightRules extends BaseRules{
    @Override
    protected void turnOwnerInfluenceModifier(Map<String, Integer> playerInfluence, String roundOwner) {
        super.turnOwnerInfluenceModifier(playerInfluence, roundOwner);
        playerInfluence.put(roundOwner,playerInfluence.getOrDefault(roundOwner,0) + 2 );
    }
}
