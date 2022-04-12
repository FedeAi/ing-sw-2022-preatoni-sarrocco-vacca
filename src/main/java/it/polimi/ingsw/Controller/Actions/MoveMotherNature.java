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
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        game.moveMotherNature(movement);
        int newMotherPosition = game.getMotherNature().getPosition();
        Island island = game.getIslandContainer().get(newMotherPosition);
        // set owner
        Optional<String> islandNewOwner_opt = computeInfluence(game);
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
        if(checkJoin(prevIsland,island)) {
            islandContainer.joinPrevIsland(newMotherPosition);
            game.moveMotherNature(-1);
        }
        Island nextIsland = islandContainer.nextIsland(newMotherPosition);
        if(checkJoin(island,nextIsland)) {
            islandContainer.joinNextIsland(newMotherPosition);
        }


        // change state
        game.setGameState(GameState.ACTION_CHOOSE_CLOUD);

    }

    private boolean checkJoin(Island island1, Island island2){
        return island1.getOwner().equals(island2.getOwner());
    }
    // todo add in Rules
    private Optional<String> computeInfluence(Game game) {
        Map<String, Integer> playerInfluence = new HashMap<>();
        int motherPosition = game.getMotherNature().getPosition();
        Map<Color, String> gameProfessors = game.getProfessors();
        Island island = game.getIslandContainer().get(motherPosition);
        for (Map.Entry<Color, Integer> islandStudentInflunce : island.getStudents().entrySet()) {
            Color student = islandStudentInflunce.getKey();
            int influence = islandStudentInflunce.getValue();
            // let's find which player has the professor of color student
            String player = gameProfessors.get(student);
            if (player != null) {
                // increment influence of player that has the professor of a specific color (student)
                playerInfluence.put(player, playerInfluence.getOrDefault(player, 0) + influence);
            }
        }
        // take in account the + island.getNumTower() given by player towers
        if (island.getNumTower() != 0) {
            playerInfluence.put(island.getOwner(), playerInfluence.getOrDefault(island.getOwner(), 0) + island.getNumTower());
        }

        // let's find the player who has the bigger influence
        Optional<Map.Entry<String, Integer>> maxPlayer = playerInfluence.entrySet().stream().max((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()));
        Optional<String> player = Optional.ofNullable(maxPlayer.isPresent() ? maxPlayer.get().getKey() : null);
        return player;
    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }
}
