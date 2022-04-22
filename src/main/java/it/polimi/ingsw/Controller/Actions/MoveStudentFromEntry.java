package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public abstract class MoveStudentFromEntry extends Performable{

    protected final Color color;

    MoveStudentFromEntry(String player, Color color){
        super(player);
        this.color = color;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if(!super.canPerformExt(game, rules)){
            return false;
        }

        Player player = getPlayer(game);

        if(!game.getGameState().equals(GameState.ACTION_MOVE_STUDENTS)){
            return false;
        }

        // if the player has already moved the Rules.getStudentsPerTurn students
        if(Rules.getEntrySize(game.numPlayers()) - player.getSchool().getEntryStudentsNum() >= Rules.getStudentsPerTurn(game.numPlayers())){
            return false;
        }

        // if the player has not a student of Color color
        if(player.getSchool().getStudentsEntry().get(color) == 0)
            return false;

        return true;
    }


}
