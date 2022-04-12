package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.FarmerRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class FarmerCharacter extends CharacterCard {

    public FarmerCharacter(String imagePath) {
        super(imagePath);
        price = 2;
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        rules.setDynamicRules(new FarmerRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        rules.setDynamicRules(new BaseRules());
    }

}
