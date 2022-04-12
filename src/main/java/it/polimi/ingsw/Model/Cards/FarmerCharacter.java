package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.FarmerRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class FarmerCharacter extends CharacterCard {
    private boolean isActive;

    public FarmerCharacter(String imagePath) {
        super(imagePath);
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        price = Rules.farmerPrice;
        rules.setDynamicRules(new FarmerRules());
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
