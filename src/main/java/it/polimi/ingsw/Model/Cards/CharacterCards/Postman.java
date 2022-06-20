package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public class Postman extends CharacterCard {
    public Postman(String imagePath) {
        super(imagePath);
        price = 1;
        character = Character.POSTMAN;
    }

    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        rules.setDynamicRules(new PostmanRules());
    }

    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        rules.setDynamicRules(new BaseRules());
    }
}
