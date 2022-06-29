package it.polimi.ingsw.controller.rules;

import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.islands.IslandContainer;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * WinController class checks if a player has won.
 */
public class WinController {

    /**
     * Method check returns the nickname of the player that has won, and null otherwise.
     *
     * @param game the game model reference.
     */
    public static String check(Game game) {

        for (Player p : game.getPlayers()) {
            if (p.getSchool().getNumTowers() <= 0) {  //first way to win has finished the tower
                return p.getNickname();
            }
            //another way to win has finished the cards but i should wait the end of the turn
            if (p.getCards().isEmpty() && game.getGameState() == GameState.PLANNING_CHOOSE_CARD) {
                return winner(game);
            }
        }
        //another way to win has finished the cards but i should wait the end of the turn
        if (game.getIslandContainer().size() <= 3 && game.getGameState() == GameState.PLANNING_CHOOSE_CARD) {
            return winner(game);
        }
        //the last fpr the win is has finished the available student in the bag
        if (game.getBag().getStudents().isEmpty()) {
            return winner(game);
        }
        return null;
    }

    /**
     * Method winner checks for the player with the most islands owned or, in case of parity, the most professors.
     *
     * @param game the game model reference.
     */
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
        // FIXME Maybe there's a smarter method to do it with streams + comparator ?
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