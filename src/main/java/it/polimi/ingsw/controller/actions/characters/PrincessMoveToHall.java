package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Princess;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.Optional;

/**
 * PrincessMoveToEntry class represent the Princess character card game action.
 * The action allows a player to select a student from the card and to move it to his school's hall.
 *
 * @see Princess
 */
public class PrincessMoveToHall extends Performable {

    private final Color student;

    /**
     * Constructor PrincessMoveToEntry creates the PrincessMoveToEntry instance,
     * and sets the student selection.
     *
     * @param player  the nickname of the action owner.
     * @param student the student to move to the player's school hall.
     */
    public PrincessMoveToHall(String player, Color student) {
        super(player);
        this.student = student;
    }

    /**
     * Method canPerform extends the Performable definition with the PrincessMoveToEntry specific checks.
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

        if (!game.getGameState().equals(GameState.PRINCESS_MOVE_STUDENT)) {
            throw new WrongStateException("state you access by activating the princess card.");
        }

        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Princess)) {
            throw new GameException("There isn't any character card of the type princess on the table.");
        }

        // There is not an active card check
        Optional<CharacterCard> card = game.getActiveCharacter(Princess.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present of the princess type.");
        }

        Player p = getPlayer(game);
        if (p.getSchool().getStudentsHall().getOrDefault(student, 0) >= Constants.SCHOOL_LANE_SIZE) {
            throw new GameException("You already have the maximum amount (" + Constants.SCHOOL_LANE_SIZE + ") of " + student + " students in your school's hall!");
        }

        // Now it's safe to cast the activated card
        Princess princess = (Princess) card.get();

        // Verify that the MONK card has a student of the specified COLOR
        if (princess.getStudentsMap().getOrDefault(student, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + student.toString() + ") on the princess card.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The princess card effect will be activated,
     * and the selected student will be moved to the player's school hall.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        // to check instance of and make cast
        Princess princess = (Princess) game.getActiveCharacter(Princess.class).get();
        princess.moveStudent(student);
        player.getSchool().addStudentHall(student);
        // Update the professor owner list
        game.setProfessors(rules.getDynamicRules().getProfessorInfluence(game));
    }
}