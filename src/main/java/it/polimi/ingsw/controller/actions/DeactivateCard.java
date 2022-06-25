package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidIndexException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;
import it.polimi.ingsw.model.Game;

/**
 * DeactivateCard class represent the character card manual deactivation game action.
 */
public class DeactivateCard extends Performable {
    private final int cardIndex;

    /**
     * Constructor DeactivateCard creates the DeactivateCard instance, and sets the character card selection by index.
     *
     * @param player the nickname of the action owner.
     * @param cardIndex the index of the character card selection.
     */
    public DeactivateCard(String player, int cardIndex) {
        super(player);
        this.cardIndex = cardIndex;
    }

    /**
     * Method canPerform extends the Performable definition with the DeactivateCard specific checks.
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
        // Simple check that verifies that there is a player with the specified name, and that he/she is the roundOwner
        super.canPerform(game, rules);
        if(cardIndex < 0 || cardIndex >= game.getCharacterCards().size()){
            throw new InvalidIndexException("character card", 0, Constants.NUM_CHARACTER_CARDS, cardIndex);
        }
        if(!game.getCharacterCards().get(cardIndex).isActive()){
            throw new GameException(game.getCharacterCards().get(cardIndex).getClass().getSimpleName() + " is not active");
        }
        if(!game.getCharacterCards().get(cardIndex).getActivatingPlayer().equals(game.getRoundOwner().getNickname())){
            throw new GameException("Only the player who activated the card may deactivate it");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The character card will be deactivated,
     * and the state will be restored to the previous state if necessary (this logic is on the card itself).
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        game.deactivateCharacterCard(cardIndex, rules);
    }
}