package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.JokerCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class JokerSwapStudents extends Performable {
    private Color studentToPick, studentToPut;

    public JokerSwapStudents(String nickName, Color studentToPick, Color studentToPut) {
        super(nickName);
        this.studentToPick = studentToPick;
        this.studentToPut = studentToPut;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerformExt(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.JOKER_SWAP_STUDENTS)) {
            return false;
        }

        // is action legal check
        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty())
            return false;

        // the active card is not the right one
        if (!(card.get() instanceof JokerCharacter)) {
            return false;
        }

        JokerCharacter joker = (JokerCharacter) card.get();

        // already done all possible movements
        if (joker.getSwappedStudents() >= JokerCharacter.maxSwaps) {
            return false;
        }

        // the card does not have that student
        if (joker.getStudents().getOrDefault(studentToPick, 0) <= 0) {
            return false;
        }
        // the player does not have that student
        if (player.getSchool().getStudentsEntry().getOrDefault(studentToPut, 0) <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public void performMove(Game game, Rules rules) {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return;
        Player player = player_opt.get();

        // to check instance of and make cast
        if (game.getActiveCharacter().isPresent() && canPerformExt(game, rules)) {
            JokerCharacter joker = (JokerCharacter) game.getActiveCharacter().get();
            joker.swapStudents(studentToPick, studentToPut);
            player.getSchool().addStudentEntry(studentToPick);
            player.getSchool().removeStudentFromEntry(studentToPut);

            // card deactivate
            if(joker.getSwappedStudents() >= JokerCharacter.maxSwaps){
                joker.deactivate(rules, game);
            }
        }
    }

}


