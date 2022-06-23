package it.polimi.ingsw.Model.Cards.CharacterCards;

import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Controller.Rules.DynamicRules.BaseRules;
import it.polimi.ingsw.Controller.Rules.DynamicRules.PostmanRules;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

/**
 * Postman class is model representation of the Postman character card.
 *
 * @see PostmanRules
 */
public class Postman extends CharacterCard {

    /**
     * Constructor Postman sets the correct Character enum type and the correct price to the card.
     */
    public Postman(String imagePath) {
        super(imagePath);
        price = 1;
        character = Character.POSTMAN;
    }

    /**
     * Method activate extends the default activate behaviour of the CharacterCard abstract with the Postman logic.
     *
     * @param rules the current rules of the game.
     * @param game  the reference to the current game.
     */
    @Override
    public void activate(Rules rules, Game game) {
        super.activate(rules, game);
        rules.setDynamicRules(new PostmanRules());
    }

    /**
     * Method deactivate extends the default deactivate behaviour of the CharacterCard abstract with the Postman logic.
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