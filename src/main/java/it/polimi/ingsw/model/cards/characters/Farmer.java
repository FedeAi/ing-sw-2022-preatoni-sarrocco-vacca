package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.dynamic.BaseRules;
import it.polimi.ingsw.controller.rules.dynamic.FarmerRules;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.Game;

/**
 * Farmer class is model representation of the Farmer character card.
 *
 * @see FarmerRules
 */
public class Farmer extends CharacterCard {

    /**
     * Constructor Farmer sets the correct Character enum type and the correct price to the card.
     */
    public Farmer() {
        super();
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