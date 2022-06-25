package it.polimi.ingsw.controller.actions.characters;

import it.polimi.ingsw.controller.actions.Performable;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.model.cards.characters.Grandma;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;

import java.util.Optional;

/**
 * GrandmaBlockIsland class represent the Grandma character card game action.
 * The action allows a player to block an island, which freeze the influence updates for a single time.
 *
 * @see Grandma
 */
public class GrandmaBlockIsland extends Performable {

    int islandIndex;

    /**
     * Constructor GrandmaBlockIsland creates the GrandmaBlockIsland instance, and sets the island selection by index.
     *
     * @param player      the nickname of the action owner.
     * @param islandIndex the index of the island selection.
     */
    public GrandmaBlockIsland(String player, int islandIndex) {
        super(player);
        this.islandIndex = islandIndex;
    }

    /**
     * Method canPerform extends the Performable definition with the GrandmaBlockIsland specific checks.
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
        if (!game.getGameState().equals(GameState.GRANDMA_BLOCK_ISLAND)) {
            throw new WrongStateException("state you access by activating the grandma card.");
        }

        // Checks if the islandIndex is correct
        if (!game.getIslandContainer().isFeasibleIndex(islandIndex)) {
            throw new InvalidIndexException("island", 0, game.getIslandContainer().size() - 1, islandIndex);
        }

        // Checks if the island isn't already blocked
        if (game.getIslandContainer().get(islandIndex).isBlocked()) {
            throw new GameException("The island has already been blocked. Please select another one.");
        }

        // We check if any of the cards on the table are of the GRANDMA type
        if (game.getCharacterCards().stream().noneMatch(characterCard -> characterCard instanceof Grandma)) {
            throw new GameException("There isn't any character card of the type grandma on the table.");
        }

        // Simple check to see if we have an active card
        Optional<CharacterCard> card = game.getActiveCharacter(CharacterCard.class);
        if (card.isEmpty()) {
            throw new GameException("There isn't any active CharacterCard present.");
        }

        // Checking if the activated card is of the GRANDMA type
        if (!(card.get() instanceof Grandma)) {
            throw new GameException("The card that has been activated in this turn is not of the grandma type.");
        }

        // Now it's safe to cast the activated card
        Grandma grandma = (Grandma) card.get();

        // Verify that we have at least 1 blockingCard to use
        if (grandma.getBlockingCards() <= 0) {
            throw new GameException("The grandma character card doesn't have any blocking cards left. Please step on a blocked island in order to use it.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The grandma card effect will be activated, and the selected island will be blocked.
     * The method also handles automatic deactivation after the execution.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        // Redundant card presence check, then we execute the action
        if (game.getActiveCharacter(Grandma.class).isPresent()) {
            Grandma grandma = (Grandma) game.getActiveCharacter(Grandma.class).get();
            grandma.moveBlockingCard();
            game.setIslandBlock(islandIndex, true);
            game.deactivateCharacterCard(game.getCharacterCards().indexOf(grandma), rules);
        }
    }
}