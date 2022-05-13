package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.MinstrelCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class MinstrelSwapStudents extends Performable {

    private Color studentFromEntry, studentFromHall;

    public MinstrelSwapStudents(String nickName, Color studentToPick, Color studentToPut) {
        super(nickName);
        this.studentFromEntry = studentToPick;
        this.studentFromHall = studentToPut;
    }

    @Override
    protected void canPerform(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerform(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.MINISTREL_SWAP_STUDENTS)) {
            return false;
        }


        // is action legal check
        // there is no an active card

        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty())
            return false;

        // the active card is not the right one
        if (!(card.get() instanceof MinstrelCharacter)) {
            return false;
        }

        MinstrelCharacter minstrel = (MinstrelCharacter) card.get();

        // already done all possible movements
        if (minstrel.getSwappedStudents() >= MinstrelCharacter.maxSwaps) {
            return false;
        }
        //the entry doesn't have enough students

        if (player.getSchool().getStudentsEntry().getOrDefault(studentFromEntry, 0) <= 0) {
            return false;
        }
        if (player.getSchool().getStudentsHall().getOrDefault(studentFromHall, 0) <= 0) {
            return false;
        }

        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {

        Optional<CharacterCard> card = game.getActiveCharacter();
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        MinstrelCharacter minstrel = (MinstrelCharacter) card.get();

        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        // to check instance of and make cast
        if (canPerform(game, rules)) {

            player.getSchool().swapStudents(studentFromEntry, studentFromHall);
            game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game)); //find new owners - professors
            minstrel.incrementSwapped();
            // coin
            int hallPosition = player.getSchool().getStudentsHall().getOrDefault(studentFromEntry, 0);
            if (Rules.checkCoin(hallPosition)) {
                game.incrementPlayerBalance(player.getNickname());

            }

        }
    }
}
