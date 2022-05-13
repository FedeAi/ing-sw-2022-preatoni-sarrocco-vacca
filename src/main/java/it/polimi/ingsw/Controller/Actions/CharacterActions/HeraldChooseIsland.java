package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.HeraldCharacter;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class HeraldChooseIsland extends Performable {
    private int islandIndex;

    public HeraldChooseIsland(String player, int islandIndex) {
        super(player);
        this.islandIndex = islandIndex;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        if (!game.getGameState().equals(GameState.HERALD_ACTIVE)) {
            throw new WrongStateException("state you access by activating the herald card.");
        }

        // is action legal check
        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // the active card is not the right one
        if (!(card.get() instanceof HeraldCharacter)) {
            throw new GameException("The card that has been activated in this turn is not of the herald type.");
        }

        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            throw new InvalidIndexException("island", 0, game.getIslandContainer().size() - 1, islandIndex);
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Island island = game.getIslandContainer().get(islandIndex);

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
        Island prevIsland = islandContainer.prevIsland(islandIndex);
        if (Island.checkJoin(prevIsland, island)) {
            islandContainer.joinPrevIsland(islandIndex);
            game.moveMotherNature(-1);
        }
        Island nextIsland = islandContainer.nextIsland(islandIndex);
        if (Island.checkJoin(island, nextIsland)) {
            islandContainer.joinNextIsland(islandIndex);
        }
    }

}
