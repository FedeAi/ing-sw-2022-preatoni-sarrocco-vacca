package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.GrandmaCharacter;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MoveMotherNature extends Performable {
    private final int movement;

    // is public correct?
    public MoveMotherNature(String player, int movement) {
        super(player);
        this.movement = movement;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerformExt(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.ACTION_MOVE_MOTHER)) {
            return false;
        }

        // is action legal check

        int playerCardMaxMoves = rules.getDynamicRules().computeMotherMaxMoves(player.getPlayedCard());
        if (movement < 1 || movement > playerCardMaxMoves) {
            return false;
        }
        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        game.moveMotherNature(movement);
        int newMotherPosition = game.getMotherNature().getPosition();
        Island island = game.getIslandContainer().get(newMotherPosition);
        if (!island.isBlocked()) {
            // set owner ( put the Tower )
            Optional<String> islandNewOwner_opt = rules.getDynamicRules().computeIslandInfluence(game, island);
            if (islandNewOwner_opt.isPresent()) {
                String islandPrevOwner = island.getOwner();
                if (!islandNewOwner_opt.get().equals(islandPrevOwner)) {
                    island.setOwner(islandNewOwner_opt.get());
                    // remove tower to the player
                    Optional<Player> islandOwnerPlayer_opt = game.getPlayerByNickname(islandNewOwner_opt.get());
                    islandOwnerPlayer_opt.ifPresent(owner -> owner.getSchool().decreaseTowers());
                    // give back the tower to the previous owner
                    Optional<Player> islandPrevPlayer_opt = game.getPlayerByNickname(islandPrevOwner);
                    islandPrevPlayer_opt.ifPresent(owner -> owner.getSchool().increaseTowers());
                }
            }
            // SuperIsland creation
            IslandContainer islandContainer = game.getIslandContainer();
            Island prevIsland = islandContainer.prevIsland(newMotherPosition);
            if (Island.checkJoin(prevIsland, island)) {
                islandContainer.joinPrevIsland(newMotherPosition);
                game.moveMotherNature(-1);
            }
            Island nextIsland = islandContainer.nextIsland(newMotherPosition);
            if (Island.checkJoin(island, nextIsland)) {
                islandContainer.joinNextIsland(newMotherPosition);
            }
        } else {
            island.setBlocked(false);
            Optional<CharacterCard> card = game.getCharacterCards().stream().filter(characterCard -> characterCard instanceof GrandmaCharacter).findFirst();
            if (card.isEmpty()) {
                return;
            }
            GrandmaCharacter grandma = (GrandmaCharacter) card.get();
            grandma.addBlockingCard();
        }
        // change state
        game.setGameState(GameState.ACTION_CHOOSE_CLOUD);
    }
}
