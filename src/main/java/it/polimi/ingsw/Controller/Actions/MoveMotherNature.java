package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MoveMotherNature implements Performable {
    private String myNickName;
    private int movement;

    MoveMotherNature(String player, int movement) {
        this.myNickName = player;
        this.movement = movement;
    }

    @Override
    public boolean canPerformExt(Game game) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return false;
        Player player = player_opt.get();

        if (!game.getRoundOwner().equals(player)) {   // if the player is not the round owner
            return false;
        }

        if (!game.getGameState().equals(GameState.ACTION_MOVE_MOTHER)) {
            return false;
        }

        // is action legal check

        int playerCardMaxMoves = player.getPlayedCard().getMaxMoves();
        if (movement < 1 || movement > playerCardMaxMoves) {
            return false;
        }
        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        // TODO WHY ARE WE CHECKING THE PLAYER, IF WE DO NOT USE IT?
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        game.moveMotherNature(movement);
        int newMotherPosition = game.getMotherNature().getPosition();
        Island island = game.getIslandContainer().get(newMotherPosition);
        // set owner ( put the Tower )
        Optional<String> islandNewOwner_opt = rules.getDynamicRules().computeIslandInfluence(game, newMotherPosition);
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
        if (checkJoin(prevIsland, island)) {
            islandContainer.joinPrevIsland(newMotherPosition);
            game.moveMotherNature(-1);
        }
        Island nextIsland = islandContainer.nextIsland(newMotherPosition);
        if (checkJoin(island, nextIsland)) {
            islandContainer.joinNextIsland(newMotherPosition);
        }


        // change state
        game.setGameState(GameState.ACTION_CHOOSE_CLOUD);

    }

    private boolean checkJoin(Island island1, Island island2) {
        if (island1.getOwner() == null || island2.getOwner() == null)
            return false;
        return island1.getOwner().equals(island2.getOwner());
    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }
}
