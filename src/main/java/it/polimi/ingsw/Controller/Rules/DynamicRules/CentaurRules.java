package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

/**
 * CentaurRules class extends the BaseRules class,
 * overriding the rules that have changed since the activation of the Centaur character card.
 */
public class CentaurRules extends BaseRules {

    /**
     * Method towerInfluenceModifier modifies the behaviour of the influence calculation:
     * as stated in the game rules when the Centaur is activated the towers must not be counter in Island influence computation.
     *
     * @see it.polimi.ingsw.Model.Cards.CharacterCards.Centaur#activate(Rules, Game)
     */
    @Override
    protected int towerInfluenceModifier(int value) {
        return 0;
    }
}