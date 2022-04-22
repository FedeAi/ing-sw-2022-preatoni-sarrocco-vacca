package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Cards.AssistantCard;

public class PostmanRules extends BaseRules {
    @Override
    public int computeMotherMaxMoves(AssistantCard card) {
        return super.computeMotherMaxMoves(card) + 2;
    }
}
