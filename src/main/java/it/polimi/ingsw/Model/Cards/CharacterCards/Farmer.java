package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.FarmerRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

/**
 * Farmer class is model representation of the Farmer character card.
 *
 * @see FarmerRules
 */
public class Farmer extends CharacterCard {

    /**
     * Constructor Farmer sets the correct Character enum type and the correct price to the card.
     */
    public Farmer(String imagePath) {
        super(imagePath);
        price = 2;
        character = Character.FARMER;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Farmer logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        rules.setDynamicRules(new FarmerRules());
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Farmer logic.
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