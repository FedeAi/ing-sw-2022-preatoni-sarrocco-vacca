package it.polimi.ingsw.controller.rules.dynamic;

import it.polimi.ingsw.model.cards.AssistantCard;

/**
 * PostmanRules class extends the BaseRules class,
 * overriding the rules that have changed since the activation of the Postman character card.
 */
public class PostmanRules extends BaseRules {

    /**
     * Method computeMotherMaxMoves adds 2 movement points to the played card's maximum moves.
     *
     * @param card the AssistantCard that has been played by the current player.
     */
    @Override
    public int computeMotherMaxMoves(AssistantCard card) {
        return super.computeMotherMaxMoves(card) + 2;
    }
}
