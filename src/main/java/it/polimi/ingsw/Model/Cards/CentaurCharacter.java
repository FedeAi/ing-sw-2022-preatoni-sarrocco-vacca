package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.CentaurRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class CentaurCharacter extends CharacterCard{

    public CentaurCharacter(String imagePath){
        super(imagePath);
        price = 3;
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        rules.setDynamicRules(new CentaurRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        rules.setDynamicRules(new BaseRules());
    }
}
