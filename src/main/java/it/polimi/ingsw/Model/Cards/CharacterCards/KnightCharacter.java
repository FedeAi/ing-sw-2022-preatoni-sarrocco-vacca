package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.KnightRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class KnightCharacter extends CharacterCard {
    public KnightCharacter(String imagePath) {
        super(imagePath);
        price = 2;
        isActive = false;
    }

    @Override
    public void init() {}

    @Override
    public void activate(Rules rules, Game game) {
        isActive = true;
        activated = true;
        rules.setDynamicRules(new KnightRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        isActive = false;
        rules.setDynamicRules(new BaseRules());
    }
}
