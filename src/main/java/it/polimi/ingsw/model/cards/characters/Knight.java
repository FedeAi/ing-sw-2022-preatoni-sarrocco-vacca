package it.polimi.ingsw.model.cards.characters;

import it.polimi.ingsw.constants.Character;
import it.polimi.ingsw.controller.rules.dynamic.BaseRules;
import it.polimi.ingsw.controller.rules.dynamic.KnightRules;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.model.Game;

/**
 * Knight class is model representation of the Knight character card.
 *
 * @see KnightRules
 */
public class Knight extends CharacterCard {

    /**
     * Constructor Knight sets the correct Character enum type and the correct price to the card.
     */
    public Knight(String imagePath) {
        super(imagePath);
        price = 2;
        character = Character.KNIGHT;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Knight logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        rules.setDynamicRules(new KnightRules());
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Knight logic.
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