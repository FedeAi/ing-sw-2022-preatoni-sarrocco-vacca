package it.polimi.ingsw.controller.rules;

import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * WinController class checks if a player has won.
 */
public class WinController {

    /**
     * Method check returns the nickname of the player that has won, and null otherwise.
     *
     * @param game the game model reference.
     * @return The nickname of the winner if present, null otherwise.
     */
    public static String check(Game game) {

        for (Player p : game.getPlayers()) {
            if (p.getSchool().getNumTowers() <= 0) {  //first way to win has finished the tower
                return p.getNickname();
            }
            //another way to win has finished the cards but I should wait the end of the turn
            if (p.getCards().isEmpty() && game.getGameState() == GameState.NEW_ROUND) {
                return winner(game);
            }
        }
        //another way to win has finished the cards but I should wait the end of the turn
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
     * @return The nickname of the winner if present, null otherwise.
     */
    private static String winner(Game game) {
        // ordering considering the number of towers
        List<Player> playersOrdered = game.getPlayers().stream().sorted((p1, p2) -> Integer.compare(p1.getSchool().getNumTowers(), p2.getSchool().getNumTowers())).toList();

        List<Player> tempOrdered = playersOrdered.stream().filter(player -> player.getSchool().getNumTowers() == playersOrdered.get(0).getSchool().getNumTowers()).toList();

        if (tempOrdered.size() == 1) {
            return tempOrdered.get(0).getNickname();
        } else {
            //ordering considering number of prof
            Stream<Player> playersOrderedP = game.getPlayers().stream().sorted((p1, p2) -> Integer.compare(countProfs(game, p1), countProfs(game, p2)));
            Optional<Player> winningPlayer = playersOrderedP.findFirst();
            if (winningPlayer.isPresent()) {
                return winningPlayer.get().getNickname();
            }
        }
        return null;
    }

    private static int countProfs(Game game, Player p) {
        return (int) game.getProfessors().entrySet().stream().filter(e -> Objects.equals(e.getValue(), p.getNickname())).count();
    }
}