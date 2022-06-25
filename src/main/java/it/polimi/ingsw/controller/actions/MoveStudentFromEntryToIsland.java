package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * MoveStudentFromEntryToIsland represents the action where a student is moved from the student entry to an island.
 *
 * @see MoveStudentFromEntry
 */
public class MoveStudentFromEntryToIsland extends MoveStudentFromEntry {

    private int islandIndex;

    /**
     * Constructor MoveStudentFromEntryToHall creates the action instance,
     * and sets the action island index.
     * @param player the nickname of the action owner.
     * @param color the selected color from the student entry to be moved to the island.
     * @param islandIndex the selected island to move the student to.
     */
    public MoveStudentFromEntryToIsland(String player, Color color, int islandIndex) {
        super(player, color);
        this.islandIndex = islandIndex;
    }

    /**
     * Method canPerform extends the Performable MoveStudentFromEntry with the specific MoveStudentFromEntryToIsland checks.
     *
     * @param game  represents the game Model.
     * @param rules represents the current game rules.
     * @throws InvalidPlayerException if the player is not in the current game.
     * @throws RoundOwnerException    if the player is not the current round owner.
     * @throws GameException          for generic errors.
     * @see MoveStudentFromEntry#canPerform(Game, Rules)
     */
    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        super.canPerform(game, rules);
        if (islandIndex < 0 || islandIndex >= game.getIslandContainer().size()) {
            throw new InvalidIndexException("island", 0, game.getIslandContainer().size() - 1, islandIndex);
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the MoveStudentFromEntryToIsland action.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @see Performable#canPerform(Game, Rules)
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        // Method that removes the specified student from the player's entry
        player.getSchool().removeStudentFromEntry(color);
        // Method that adds the specified student to the player's selected island
        game.addIslandStudent(islandIndex, color);
    }
}