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

/**
 * JokerSwapStudents class represent the Joker character card game action.
 * The action allows a player to swap a maximum of 3 students between his school's entry and the card.
 *
 * @see Joker
 */
public class JokerSwapStudents extends Performable {

    private Color studentToPick, studentToPut;

    /**
     * Constructor JokerSwapStudents creates the JokerSwapStudents instance,
     * and sets the entry and character card student selection.
     *
     * @param player        the nickname of the action owner.
     * @param studentToPick the student to be picked from the card and put into the school's entry.
     * @param studentToPut  the student to be picked from the entry and swapped to the card.
     */
    public JokerSwapStudents(String player, Color studentToPick, Color studentToPut) {
        super(player);
        this.studentToPick = studentToPick;
        this.studentToPut = studentToPut;
    }

    /**
     * Method canPerform extends the Performable definition with the JokerSwapStudent specific checks.
     *
     * @param game  represents the game Model.
     * @param rules represents the current game rules.
     * @throws InvalidPlayerException if the player is not in the current game.
     * @throws RoundOwnerException    if the player is not the current round owner.
     * @throws GameException          for generic errors.
     * @see Performable#canPerform(Game, Rules)
     */
    @Override
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he is the roundOwner
        super.canPerform(game, rules);

        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.JOKER_SWAP_STUDENTS)) {
            throw new WrongStateException("state you access by activating the joker card.");
        }

        // There is not an active card check
        Optional<CharacterCard> card = game.getActiveCharacter(Joker.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // The active card is not of the right type
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

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The joker card effect will be activated,
     * the swap between the selected student from the entry and from the card will be executed.
     * The method also handles automatic deactivation after the maximum swaps.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
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