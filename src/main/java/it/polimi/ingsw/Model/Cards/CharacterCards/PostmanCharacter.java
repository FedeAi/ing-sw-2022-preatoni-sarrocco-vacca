package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class PostmanCharacter extends CharacterCard {
    public PostmanCharacter(String imagePath) {
        super(imagePath);
        price = 1;
        isActive = false;
    }

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        rules.setDynamicRules(new PostmanRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        rules.setDynamicRules(new BaseRules());
    }
}
