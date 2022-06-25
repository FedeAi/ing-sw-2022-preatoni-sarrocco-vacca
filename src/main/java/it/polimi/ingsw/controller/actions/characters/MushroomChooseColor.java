package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.exceptions.WrongStateException;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Mushroom;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

import java.util.Optional;

/**
 * MushroomChooseColor class represent the Mushroom character card game action.
 * The action allows a player to select a student color that will to count towards influence points for the turn.
 *
 * @see Mushroom
 */
public class MushroomChooseColor extends Performable {

    private final Color student;

    /**
     * Constructor MushroomChooseColor creates the MushroomChooseColor instance,
     * and sets student color selection.
     *
     * @param player  the nickname of the action owner.
     * @param student the student to disable the influence points on.
     */
    public MushroomChooseColor(String player, Color student) {
        super(player);
        this.student = student;
    }

    /**
     * Method canPerform extends the Performable definition with the MushroomChooseColor specific checks.
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

        if (!game.getGameState().equals(GameState.MUSHROOM_CHOOSE_COLOR)) {
            throw new WrongStateException("state you access by activating the mushroom card.");
        }

        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Mushroom)) {
            throw new GameException("There isn't any character card of the type mushroom on the table.");
        }

        // There is not an active card check
        Optional<CharacterCard> card = game.getActiveCharacter(Mushroom.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active card present.");
        }

        // The active card is not of the right type
        if (!(card.get() instanceof Mushroom)) {
            throw new GameException("The card that has been activated in this turn is not of the mushroom type.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The mushroom card effect will be activated,
     * and the selected student color influence points will be disabled for the turn.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @see it.polimi.ingsw.controller.rules.dynamic.MushroomRules
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Optional<CharacterCard> mushroom = game.getActiveCharacter(Mushroom.class);
        mushroom.ifPresent(characterCard -> ((Mushroom) characterCard).setStudent(this.student));
    }

    /**
     * Method nextState determines the next game state after a MushroomChooseColor action is executed.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @return the state previous to the selection if the action is performable, the current state otherwise.
     */
    @Override
    public GameState nextState(Game game, Rules rules) {
        try {
            canPerform(game, rules);
        } catch (Exception e) {
            return game.getGameState();
        }
        Mushroom mushroom = (Mushroom) game.getActiveCharacter(Mushroom.class).get();
        return mushroom.getPreviousState();
    }
}