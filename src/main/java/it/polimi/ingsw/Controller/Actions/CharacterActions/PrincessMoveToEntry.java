package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.PrincessCharacter;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class PrincessMoveToEntry extends Performable {

    private final Color student;

    public PrincessMoveToEntry(String nickName, Color student) {
        super(nickName);
        this.student = student;
    }

    @Override
    public boolean canPerformExt(Game game, Rules rules) {

        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        if (!super.canPerformExt(game, rules)) {
            return false;
        }

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.PRINCESS_MOVE_STUDENT)) {
            return false;
        }

        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty())
            return false;

        // the active card is not the right one
        if (!(card.get() instanceof PrincessCharacter)) {
            return false;
        }

        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof PrincessCharacter)) {
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
            PrincessCharacter princessCharacter = (PrincessCharacter) game.getActiveCharacter().get();
            princessCharacter.moveStudent(student);
            player.getSchool().addStudentHall(student);
            game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game)); //find new owners - professors
        }

    }

}

