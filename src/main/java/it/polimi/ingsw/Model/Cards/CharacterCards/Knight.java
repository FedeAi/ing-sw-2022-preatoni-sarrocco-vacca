package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.KnightRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

/**
 * Knight class is model representation of the Knight character card.
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