package it.polimi.ingsw.Controller.Rules.DynamicRules;

public class CentaurRules extends BaseRules {
    /**
     * as stated in rules when Centaur Character is activated Towers are no longer included in Island influence
     * computation
     *
     * @param value
     * @return
     */
    @Override
    protected int towerInfluenceModifier(int value) {
        return 0;
    }
}
