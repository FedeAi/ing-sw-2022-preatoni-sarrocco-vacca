package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * MoveStudentFromEntryToHall represents the action where a student is moved from the student entry to the hall.
 *
 * @see MoveStudentFromEntry
 */
public class MoveStudentFromEntryToHall extends MoveStudentFromEntry {

    /**
     * Constructor MoveStudentFromEntryToHall creates the action instance.
     *
     * @param player the nickname of the action owner.
     * @param color  the selected color from the student entry to be moved to the hall.
     */
    public MoveStudentFromEntryToHall(String player, Color color) {
        super(player, color);
    }

    /**
     * Method canPerform extends the Performable MoveStudentFromEntry with the specific MoveStudentFromEntryToHall checks.
     *
     * @param game  represents the game Model.
     * @param rules represents the current game rules.
     * @throws InvalidPlayerException if the player is not in the current game.
     * @throws RoundOwnerException    if the player is not the current round owner.
     * @throws GameException          for generic errors.
     * @see MoveStudentFromEntry#canPerform(Game, Rules)
     */
    @Override
    protected void canPerform(Game game, Rules rules) throws GameException, InvalidPlayerException, RoundOwnerException {
        super.canPerform(game, rules);
        Player p = getPlayer(game);
        if (p.getSchool().getStudentsHall().getOrDefault(color, 0) >= Constants.SCHOOL_LANE_SIZE) {
            throw new GameException("You already have the maximum amount (" + Constants.SCHOOL_LANE_SIZE + ") of " + color + " students in your school's hall!");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the MoveStudentFromEntryToHall action.
     * It also checks for the coin addition to the player's balance.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @see Performable#canPerform(Game, Rules)
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        // model modification
        player.getSchool().moveStudentFromEntryToHall(color);
        // coin
        int hallPosition = player.getSchool().getStudentsHall().getOrDefault(color, 0);
        if (Rules.checkCoin(hallPosition)) {
            game.incrementPlayerBalance(player.getNickname());
        }
        // Updates the professors to the new owners (if any)
        game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game));
    }
}