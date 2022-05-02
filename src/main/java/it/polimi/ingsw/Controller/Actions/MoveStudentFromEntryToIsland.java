package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
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
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        player.getSchool().removeStudentFromEntry(color);
        game.addIslandStudent(islandIndex, color);

        if (Rules.getEntrySize(game.numPlayers()) - player.getSchool().getEntryStudentsNum() >= Rules.getStudentsPerTurn(game.numPlayers())) {
            game.setGameState(GameState.ACTION_MOVE_MOTHER);
        }
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        if (!super.canPerformExt(game, rules)) {
            return false;
        }
        if (islandIndex < 0 || islandIndex >= game.getIslandContainer().size()) {
            return false;
        }
        return true;
    }
}
