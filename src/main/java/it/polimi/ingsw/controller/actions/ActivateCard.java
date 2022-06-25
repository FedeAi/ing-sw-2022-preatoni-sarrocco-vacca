package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.characters.CharacterCard;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * ActivateCard class represent the character card activation game action.
 */
public class ActivateCard extends Performable {

    private final int choice;

    /**
     * Constructor ActivateCard creates the ActivateCard instance, and sets the character card selection by index.
     *
     * @param player the nickname of the action owner.
     * @param choice the index of the character card selection.
     */
    public ActivateCard(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    /**
     * Method canPerform extends the Performable definition with the ActivateCard specific checks.
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

        // Simple check to verify that we're in the correct state
        if (!game.getGameState().equals(GameState.ACTION_MOVE_MOTHER) && !game.getGameState().equals(GameState.ACTION_MOVE_STUDENTS)) {
            throw new WrongStateException("action phase!");
        }

        // Verify that we are under expert rules
        if (!game.isExpertMode()) {
            throw new GameException("This feature is available only in expert mode!");
        }

        if (choice < 0 || choice >= Constants.NUM_CHARACTER_CARDS) {
            throw new InvalidIndexException("character card", 0, Constants.NUM_CHARACTER_CARDS - 1, choice);
        }

        CharacterCard chosenCard = game.getCharacterCards().get(choice);
        // Verify that the player has enough money
        if (player.getBalance() < chosenCard.getPrice()) {
            throw new GameException("You don't have enough money! The selected card costs " + chosenCard.getPrice() + ".");
        }
        if (chosenCard.isActive()) {
            throw new GameException("The selected card is already active.");
        }
        if (chosenCard.getActivatingPlayer().equals(this.player)) {
            throw new GameException("You can activate a card once per turn.");
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The character card will be activated, and the state will be changed if necessary (this logic is on the card itself).
     * The method is also responsible for the removal of coins from the player that has activated the card.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        CharacterCard chosenCard = game.getCharacterCards().get(choice);

        if (chosenCard.alreadyActivatedOnce()) {
            game.incrementBalance(chosenCard.getPrice());
        } else {
            game.incrementBalance(chosenCard.getPrice() - 1);
        }
        player.spendCoins(chosenCard.getPrice());
        game.activateCharacterCard(choice, rules);
    }
}