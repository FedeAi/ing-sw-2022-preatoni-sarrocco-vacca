package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Joker;
import it.polimi.ingsw.model.cards.characters.Minstrel;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.Optional;

/**
 * MinstrelSwapStudents class represent the Minstrel character card game action.
 * The action allows a player to swap a maximum of 2 students between his school's entry and hall.
 *
 * @see Minstrel
 */
public class MinstrelSwapStudents extends Performable {

    private Color studentFromEntry, studentFromHall;

    /**
     * Constructor MinstrelSwapStudents creates the MinstrelSwapStudents instance,
     * and sets the entry and hall student selection.
     *
     * @param player           the nickname of the action owner.
     * @param studentFromEntry the student to be picked from the school's entry.
     * @param studentFromHall  the student to be picked from the hall and swapped to the entry.
     */
    public MinstrelSwapStudents(String player, Color studentFromEntry, Color studentFromHall) {
        super(player);
        this.studentFromEntry = studentFromEntry;
        this.studentFromHall = studentFromHall;
    }

    /**
     * Method canPerform extends the Performable definition with the MinstrelSwapStudents specific checks.
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

        if (!game.getGameState().equals(GameState.MINSTREL_SWAP_STUDENTS)) {
            throw new WrongStateException("state you access by activating the minstrel card.");
        }
        // There is not an active card check
        Optional<CharacterCard> card = game.getActiveCharacter(Minstrel.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present of the minstrel type.");
        }

        Minstrel minstrel = (Minstrel) card.get();

        // The user has already done all possible swaps
        if (minstrel.getSwappedStudents() >= Minstrel.maxSwaps) {
            throw new GameException("You already swapped from the minstrel the maximum amount of students allowed per card activation.");
        }

        // The entry doesn't have enough students
        if (player.getSchool().getStudentsEntry().getOrDefault(studentFromEntry, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + studentFromEntry.toString() + ") in your school's entry.");
        }
        // The hall doesn't have enough students
        if (player.getSchool().getStudentsHall().getOrDefault(studentFromHall, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + studentFromHall.toString() + ") in your school's hall.");
        }

        Player p = getPlayer(game);
        if (p.getSchool().getStudentsHall().getOrDefault(studentFromEntry, 0) >= Constants.SCHOOL_LANE_SIZE) {
            throw new GameException("You already have the maximum amount (" + Constants.SCHOOL_LANE_SIZE + ") of " + studentFromEntry + " students in your school's hall!");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The minstrel card effect will be activated,
     * the swap between the selected student from the entry and from the hall will be executed.
     * The method also handles automatic deactivation after the maximum swaps.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);

        Optional<CharacterCard> card = game.getActiveCharacter(Minstrel.class);
        Minstrel minstrel = (Minstrel) card.get();
        Player player = getPlayer(game);

        player.getSchool().swapStudents(studentFromEntry, studentFromHall);
        game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game)); //find new owners - professors
        minstrel.incrementSwapped();

        // Coin logic
        int hallPosition = player.getSchool().getStudentsHall().getOrDefault(studentFromEntry, 0);
        if (Rules.checkCoin(hallPosition)) {
            game.incrementPlayerBalance(player.getNickname());
        }
        // card deactivate
        if (minstrel.getSwappedStudents() >= Minstrel.maxSwaps) {
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(minstrel), rules);
        }
    }
}