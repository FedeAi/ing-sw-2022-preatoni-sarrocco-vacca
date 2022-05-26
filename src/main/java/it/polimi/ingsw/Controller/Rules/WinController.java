package it.polimi.ingsw.Controller.Rules;

import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.HashMap;
import java.util.Map;

public class WinController {
    public static String check(Game game) {
        for (Player p : game.getPlayers()) {
            if (p.getSchool().getNumTowers() <= 0) {
                return p.getNickname();
            }
            if (p.getCards().isEmpty()) {
                winner(game);
            }
        }
        if (game.getIslandContainer().size() <= 3) {
            return winner(game);
        }
        if (game.getBag().getStudents().isEmpty()) {
            return winner(game);
        }
        return null;
    }

    // TODO REVIEW AND TEST THIS!
    private static String winner(Game game) {
        IslandContainer container = game.getIslandContainer();
        Map<String, Integer> towerMap = new HashMap<>();
        for (int i = 0; i < container.size(); i++) {
            String owner = container.get(i).getOwner();
            if (owner != null) {
                towerMap.put(owner, towerMap.getOrDefault(owner, 0) + 1);
            }
        }
        // Maybe there's a smarter method to do it with streams + comparator ?
        int max = 0;
        String winner = null;
        Map<String, Integer> profMap = new HashMap<>();
        for (Color c : Color.values()) {
            if (game.getProfessors().get(c) != null) {
                String player = game.getProfessors().get(c);
                profMap.put(player, profMap.getOrDefault(player, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : towerMap.entrySet()) {
            if (entry.getValue() > max) {
                winner = entry.getKey();
                max = entry.getValue();
            }
            if (entry.getValue() == max) {
                if (profMap.getOrDefault(entry.getKey(), 0) > profMap.getOrDefault(winner, 0)) {
                    winner = entry.getKey();
                }
            }
        }
        return winner;
    }
}
