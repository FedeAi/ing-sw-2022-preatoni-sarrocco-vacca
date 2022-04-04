package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MoveStudentFromEntryToHall implements Performable{
    private final String myNickName;
    private final Color color;
    MoveStudentFromEntryToHall(String player, Color color){
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

        if(!game.getGameState().equals(GameState.PLANNING_PHASE_MOVE_STUDENTS_TO_HALL)){
            return false;
        }

        // if the player has already moved the 4 students
        if(Rules.getEntrySize(game.numPlayers()) - player.getSchool().getStudentsEntry().size() >= Rules.getStudentsPerTurn(game.numPlayers())){
            return false;
        }

        // if the player has not a student of Color color
        if(player.getSchool().getStudentsEntry().get(color) == 0)
            return false;

        return true;
    }

    @Override
    public void performMove(Game game) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if(player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        player.getSchool().moveStudentFromEntryToHall(color);   // model modification
        // todo check professors

        if(Rules.getEntrySize(game.numPlayers()) - player.getSchool().getStudentsEntry().size() >= Rules.getStudentsPerTurn(game.numPlayers())){
            // todo change phase
        }
    }

    @Override
    public String getNickNamePlayer() {
        return myNickName;
    }
}
