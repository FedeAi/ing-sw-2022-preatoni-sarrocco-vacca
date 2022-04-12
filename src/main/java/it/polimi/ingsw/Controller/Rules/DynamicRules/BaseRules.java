package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BaseRules implements DynamicRules{

    @Override
    public boolean influenceComparator(int influence1, int influence2) {
        return Integer.compare(influence1, influence2) > 0;
    }

    @Override
    public boolean checkProfessorInfluence(Game game, Color color) {
        String playerNickname = game.getRoundOwner().getNickname();
        String ownerNickname = game.getProfessors().get(color);
        Island motherNatureIsland = game.getIslandContainer().get( game.getMotherNature().getPosition());
        Map<Color, Integer> studentsOnIsland = motherNatureIsland.getStudents();

        if (ownerNickname == null || playerNickname.equals(ownerNickname)) {
            //professors.put(color, playerNickname);
            return true;
        }

        //get the color of most present students
        Optional<Map.Entry<Color, Integer>> mostPresentColor_opt = studentsOnIsland.entrySet().stream().max((a, b)->Integer.compare(a.getValue(),b.getValue()));

        if(mostPresentColor_opt.isEmpty())  // the island is empty
            return true;

        Color mostPresentColor = mostPresentColor_opt.get().getKey();
        int mostPresentColorQty = mostPresentColor_opt.get().getValue();
        if(mostPresentColorQty==0)
            return true;

        // dynamic comparator
        boolean influence = influenceComparator(studentsOnIsland.getOrDefault(color,0), mostPresentColorQty);
        if(influence){
            return true;
        }

        return false;
    }

    @Override
    public Optional<String> computeIslandInfluence(Game game, int islandIndex) {
        if (game.getIslandContainer().isFeasibleIndex(islandIndex)) { // perform only if the island index is ok
            Map<String, Integer> playerInfluence = new HashMap<>();
            Map<Color, String> professors = game.getProfessors();
            Island island = game.getIslandContainer().get(islandIndex);

            for (Map.Entry<Color, Integer> islandStudentInflunce : island.getStudents().entrySet()) {
                Color student = islandStudentInflunce.getKey();
                int influence = islandStudentInflunce.getValue();
                // let's find which player has the professor of color student
                String player = professors.get(student);
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
        return Optional.empty();
    }

}
