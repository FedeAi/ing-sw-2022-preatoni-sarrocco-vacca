package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Joker;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
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
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.JOKER_SWAP_STUDENTS)) {
            throw new WrongStateException("state you access by activating the joker card.");
        }

        // is action legal check
        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter(Joker.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // the active card is not the right one
        if (!(card.get() instanceof Joker)) {
            throw new GameException("The card that has been activated in this turn is not of the joker type.");
        }

        Joker joker = (Joker) card.get();

        // The player has already moved the allowed students
        if (joker.getSwappedStudents() >= Joker.maxSwaps) {
            throw new GameException("You already swapped from the joker the maximum amount of students allowed per card activation.");
        }

        // the card does not have that student
        if (joker.getStudentsMap().getOrDefault(studentToPick, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + studentToPick.toString() + ") on the joker card.");
        }
        // the player does not have that student
        if (player.getSchool().getStudentsEntry().getOrDefault(studentToPut, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + studentToPick.toString() + ") in your school's entry.");
        }
    }

    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);

        // to check instance of and make cast
        Joker joker = (Joker) game.getActiveCharacter(Joker.class).get();
        joker.swapStudents(studentToPick, studentToPut);
        player.getSchool().addStudentEntry(studentToPick);
        player.getSchool().removeStudentFromEntry(studentToPut);

        // card deactivate
        if (joker.getSwappedStudents() >= Joker.maxSwaps) {
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(joker), rules);
        }
    }
}


