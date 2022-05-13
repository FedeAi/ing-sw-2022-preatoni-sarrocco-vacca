package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MoveStudentFromEntryToIsland extends MoveStudentFromEntry {

    private int islandIndex;

    public MoveStudentFromEntryToIsland(String player, Color color, int islandIndex) {
        super(player, color);
        this.islandIndex = islandIndex;
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        // Method that removes the specified student from the player's entry
        player.getSchool().removeStudentFromEntry(color);
        // Method that adds the specified student to the player's selected island
        game.addIslandStudent(islandIndex, color);
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        super.canPerform(game, rules);
        if (islandIndex < 0 || islandIndex >= game.getIslandContainer().size()) {
            throw new GameException("Invalid island index! You can only select islands from 0 to " + game.getIslandContainer().size());
        }
    }
}
