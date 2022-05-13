package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
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
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        if (!game.getGameState().equals(GameState.PRINCESS_MOVE_STUDENT)) {
            throw new WrongStateException("state you access by activating the princess card.");
        }

        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter();
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // the active card is not the right one
        if (!(card.get() instanceof PrincessCharacter)) {
            throw new GameException("The card that has been activated in this turn is not of the princess type.");
        }

        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof PrincessCharacter)) {
            throw new GameException("There isn't any character card of the type princess on the table.");
        }
    }


    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        // to check instance of and make cast
        if (game.getActiveCharacter().isPresent()) {
            PrincessCharacter princessCharacter = (PrincessCharacter) game.getActiveCharacter().get();
            princessCharacter.moveStudent(student);
            player.getSchool().addStudentHall(student);
            game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game)); //find new owners - professors
        }
    }
}

