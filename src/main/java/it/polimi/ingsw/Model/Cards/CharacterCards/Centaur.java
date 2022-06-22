package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.CentaurRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

/**
 * Centaur class is model representation of the Centaur character card.
 */
public class Centaur extends CharacterCard {

    /**
     * Constructor Centaur sets the correct Character enum type and the correct price to the card.
     */
    public Centaur(String imagePath) {
        super(imagePath);
        price = 3;
        character = Character.CENTAUR;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Centaur logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        rules.setDynamicRules(new CentaurRules());
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Centaur logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void deactivate(Rules rules, Game game) {
        super.deactivate(rules, game);
        rules.setDynamicRules(new BaseRules());
    }
}