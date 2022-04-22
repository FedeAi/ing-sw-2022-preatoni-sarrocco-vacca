package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.JockerCharacter;
import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class JockerSwapStudents extends Performable {
    private Color studentToPick, studentToPut;
    public JockerSwapStudents(String nickName, Color studentToPick, Color studentToPut) {
        super(nickName);
        this.studentToPick = studentToPick;
        this.studentToPut = studentToPut;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if(!super.canPerformExt(game, rules)){
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.JOKER_SWAP_STUDENTS)) {
            return false;
        }

        // is action legal check
        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if(card.isEmpty())
            return false;

        // the active card is not the right one
        if(!(card.get() instanceof JockerCharacter)){
           return false;
        }
        JockerCharacter jocker = (JockerCharacter) card.get();

        // already done all possible movements
        if(jocker.getSwappedStudents() >= JockerCharacter.maxSwaps) {
            return false;
        }

        // the card does not have that student
        if(jocker.getStudents().getOrDefault(studentToPick, 0) <= 0){
            return false;
        }
        // the player does not have that student
        if(player.getSchool().getStudentsEntry().getOrDefault(studentToPut,0) <= 0){
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
        if(game.getActiveCharacter().isPresent() && canPerformExt(game, rules)){
            JockerCharacter jocker = (JockerCharacter) game.getActiveCharacter().get();
            jocker.swapStudents(studentToPick, studentToPut);
            player.getSchool().addStudentEntry(studentToPick);
            player.getSchool().removeStudentFromEntry(studentToPut);
        }


    }

}


