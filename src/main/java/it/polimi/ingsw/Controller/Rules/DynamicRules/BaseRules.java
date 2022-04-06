package it.polimi.ingsw.Controller.Rules.DynamicRules;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Island;

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
        Island motherNatureIsland = game.getIslands().get( game.getMotherNature().getPosition());
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
}
