package it.polimi.ingsw.Controller.Actions.CharacterActions;

import it.polimi.ingsw.Controller.Actions.Performable;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.Monk;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Model.Game;

import java.util.Optional;

/**
 * MonkMoveToIsland class represent the Monk character card game action.
 * The action allows a player to put a student from the card to a certain island.
 *
 * @see Monk
 */
public class MonkMoveToIsland extends Performable {

    private Color student;
    private int islandIndex;

    /**
     * Constructor MonkMoveToIsland creates the MonkMoveToIsland instance,
     * and sets the entry and character card student selection.
     *
     * @param player      the nickname of the action owner.
     * @param student     the student to be picked from the card and put on the island.
     * @param islandIndex the selected island index.
     */
    public MonkMoveToIsland(String player, Color student, int islandIndex) {
        super(player);
        this.student = student;
        this.islandIndex = islandIndex;
    }

    /**
     * Method canPerform extends the Performable definition with the MonkMoveToIsland specific checks.
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

        // Checks if the game is set to the correct state
        if (!game.getGameState().equals(GameState.MONK_MOVE_STUDENT)) {
            throw new WrongStateException("state you access by activating the monk card.");
        }

        // Checks if the islandIndex is correct
        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            throw new InvalidIndexException("island", 0, game.getIslandContainer().size(), islandIndex);
        }

        // We check if any of the cards on the table are of the MONK type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Monk)) {
            throw new GameException("There isn't any character card of the type monk on the table.");
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter(Monk.class);
        if (card.isEmpty()) {
            throw new GameException("The monk character isn't active.");
        }

        // Checking if the activated card is of the MONK type
        if (!(card.get() instanceof Monk)) {
            throw new GameException("The card that has been activated in this turn is not of the monk type.");
        }

        // Now it's safe to cast the activated card
        Monk monk = (Monk) card.get();

        // Verify that the MONK card has a student of the specified COLOR
        if (monk.getStudentsMap().getOrDefault(student, 0) <= 0) {
            throw new GameException("There isn't any student of the specified color (" + student.toString() + ") on the monk card.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The monk card effect will be activated, and the card's selected student will be put on the island.
     * The method also handles automatic deactivation after the action.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        // Redundant card presence check and general canPerform() check, then we execute the action
        if (game.getActiveCharacter(Monk.class).isPresent()) {
            Monk monk = (Monk) game.getActiveCharacter(Monk.class).get();
            monk.moveStudent(student);
            // Now we add the student to the specified island
            game.addIslandStudent(islandIndex, student);
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(monk), rules);
        }
    }
}