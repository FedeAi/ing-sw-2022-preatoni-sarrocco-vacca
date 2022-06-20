package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.CentaurRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class Centaur extends CharacterCard {

    public Centaur(String imagePath) {
        super(imagePath);
        price = 3;
        character = Character.CENTAUR;
    }

    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        rules.setDynamicRules(new CentaurRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        rules.setDynamicRules(new BaseRules());
    }
}
