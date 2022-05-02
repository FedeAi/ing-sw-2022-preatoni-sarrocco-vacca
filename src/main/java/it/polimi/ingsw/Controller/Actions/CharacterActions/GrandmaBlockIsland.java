package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.GrandmaCharacter;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class GrandmaBlockIsland extends Performable {

    int islandIndex;

    public GrandmaBlockIsland(String nickName, int islandIndex) {
        super(nickName);
        this.islandIndex = islandIndex;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        if (!super.canPerformExt(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.GRANDMA_BLOCK_ISLAND)) {
            return false;
        }

        // Checks if the islandIndex is correct
        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            return false;
        }

        // Checks if the island isn't already blocked
        if (game.getIslandContainer().get(islandIndex).isBlocked()) {
            return false;
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            return false;
        }

        // We check if any of the cards on the table are of the GRANDMA type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof GrandmaCharacter)) {
            return false;
        }

        // Checking if the activated card is of the GRANDMA type
        if (!(card.get() instanceof GrandmaCharacter)) {
            return false;
        }

        // Now it's safe to cast the activated card
        GrandmaCharacter grandma = (GrandmaCharacter) card.get();

        // Verify that we have at least 1 blockingCard to use
        if (grandma.getBlockingCards() <= 0) {
            return false;
        }

        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        // Redundant card presence check and general canPerform() check, then we execute the action
        if (game.getActiveCharacter().isPresent() && canPerformExt(game, rules)) {
            GrandmaCharacter grandma = (GrandmaCharacter) game.getActiveCharacter().get();
            grandma.moveBlockingCard();
            game.getIslandContainer().get(islandIndex).setBlocked(true);
            grandma.deactivate(rules, game);
        }
    }
}
