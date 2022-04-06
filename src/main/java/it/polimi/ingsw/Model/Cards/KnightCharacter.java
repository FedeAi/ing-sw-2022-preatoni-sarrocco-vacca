package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.KnightRules;
import it.polimi.ingsw.Controller.Rules.Rules;

public class KnightCharacter extends CharacterCard{
    private boolean isActive;
    public KnightCharacter(String imagePath) {
        super(imagePath);
        isActive = false;
    }

    @Override
    public void activate(Rules rules) {
        isActive = true;
        rules.setDynamicRules(new KnightRules());
    }

    @Override
    public void deactivate(Rules rules) {
        isActive = false;
        rules.setDynamicRules(new BaseRules());
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

}
