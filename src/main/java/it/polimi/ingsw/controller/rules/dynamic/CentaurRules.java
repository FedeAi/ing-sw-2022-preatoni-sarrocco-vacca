package it.polimi.ingsw.controller.rules.dynamic;

import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.Game;

/**
 * CentaurRules class extends the BaseRules class,
 * overriding the rules that have changed since the activation of the Centaur character card.
 */
public class CentaurRules extends BaseRules {

    /**
     * Method towerInfluenceModifier modifies the behaviour of the influence calculation:
     * as stated in the game rules when the Centaur is activated the towers must not be counter in Island influence computation.
     *
     * @see it.polimi.ingsw.model.cards.characters.Centaur#activate(Rules, Game)
     */
    @Override
    protected int towerInfluenceModifier(int value) {
        return 0;
    }
}