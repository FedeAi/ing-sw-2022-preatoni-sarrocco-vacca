package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MoveStudentFromEntryToHall extends MoveStudentFromEntry {

    MoveStudentFromEntryToHall(String player, Color color) {
        super(player, color);
    }

    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        player.getSchool().moveStudentFromEntryToHall(color);   // model modification

        // coin
        int hallPosition = player.getSchool().getStudentsHall().getOrDefault(color, 0);
        if (Rules.checkCoin(hallPosition)) {
            game.incrementPlayerBalance(player.getNickname());
        }
        //compute the new professors
        game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game));

        if (Rules.getEntrySize(game.numPlayers()) - player.getSchool().getEntryStudentsNum() >= Rules.getStudentsPerTurn(game.numPlayers())) {
            game.setGameState(GameState.ACTION_MOVE_MOTHER);
        }
    }
}
