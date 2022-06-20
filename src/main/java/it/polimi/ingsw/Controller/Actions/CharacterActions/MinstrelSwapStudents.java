package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Exceptions.WrongStateException;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Minstrel;
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
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);
        Player player = getPlayer(game);

        if (!game.getGameState().equals(GameState.MINSTREL_SWAP_STUDENTS)) {
            throw new WrongStateException("state you access by activating the minstrel card.");
        }

        // is action legal check
        // there is no an active card
        Optional<CharacterCard> card = game.getActiveCharacter(Minstrel.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present of the minstrel type.");
        }

        Minstrel minstrel = (Minstrel) card.get();

        // already done all possible movements
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
    }
}
