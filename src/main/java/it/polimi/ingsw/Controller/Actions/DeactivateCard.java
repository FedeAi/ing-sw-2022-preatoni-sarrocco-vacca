package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

/**
 * It is used when a player decide not to go on with the effect of the active character Card (per i fino a)
 */
public class DeactivateCard extends Performable {

    DeactivateCard(String player) {
        super(player);
    }

    @Override
    protected void canPerform(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerform(game, rules)) {
            return false;
        }

        return game.getActiveCharacter().isPresent();
    }

    @Override
    public void performMove(Game game, Rules rules) {
        game.getActiveCharacter().ifPresent(characterCard -> characterCard.deactivate(rules, game));
    }


}
