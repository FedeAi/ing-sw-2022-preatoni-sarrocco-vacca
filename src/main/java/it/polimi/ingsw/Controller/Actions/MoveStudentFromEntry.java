package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public abstract class MoveStudentFromEntry implements Performable{

    protected final String myNickName;
    protected final Color color;

    MoveStudentFromEntry(String player, Color color){
        this.myNickName = player;
        this.color = color;
    }

    @Override
    public boolean canPerformExt(Game game) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if(player_opt.isEmpty())    // if there is no Player with that nick
            return false;
        Player player = player_opt.get();

        if(!game.getRoundOwner().equals(player)){   // if the player is not the round owner
            return false;
        }

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

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }

}
