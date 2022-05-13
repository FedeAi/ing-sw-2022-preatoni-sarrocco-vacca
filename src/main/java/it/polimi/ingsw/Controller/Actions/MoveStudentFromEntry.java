package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

public abstract class MoveStudentFromEntry extends Performable {

    protected final Color color;

    MoveStudentFromEntry(String player, Color color) {
        super(player);
        this.color = color;
    }

    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.ACTION_MOVE_STUDENTS)) {
            throw new WrongStateException(GameState.ACTION_MOVE_STUDENTS);
        }

        // Checks if the player has already moved the maximum allowed students
        if (Rules.getEntrySize(game.numPlayers()) - player.getSchool().getEntryStudentsNum() >= Rules.getStudentsPerTurn(game.numPlayers())) {
            throw new GameException("You already moved " + Rules.getStudentsPerTurn(game.numPlayers()) + " students, the maximum allowed");
        }

        // Checks if the player has not a student of Color color
        if (player.getSchool().getStudentsEntry().get(color) == 0) {
            throw new GameException("You don't have any students of the selected color");
        }
    }
}
