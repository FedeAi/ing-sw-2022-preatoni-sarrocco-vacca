package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * ChooseMagician class represent the game action for selecting a magician before starting the game.
 */
public class ChooseMagician extends Performable {

    private final int choice;

    /**
     * Constructor ChooseMagician creates the ChooseMagician instance, and sets the magician selection by index.
     *
     * @param player the nickname of the action owner.
     * @param choice the index of the magician selected.
     */
    public ChooseMagician(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    /**
     * Method canPerform extends the Performable definition with the ChooseMagician specific checks.
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
        if (!game.getGameState().equals(GameState.SETUP_CHOOSE_MAGICIAN)) {
            throw new WrongStateException("You first need to set your magician");
        }
        // The number of available magicians
        int numAvailableMagicians = game.getAvailableMagicians().size();

        if (getPlayer(game).getMagician() != null) {
            throw new GameException("You have already selected your magician");
        }

        if (choice >= numAvailableMagicians || choice < 0) {
            throw new InvalidIndexException("magician", 0, numAvailableMagicians - 1, choice);
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The magician will be selected and set to the Player,
     * and then removed from the available magicians list.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);
        // Setting the player choice
        game.chooseMagician(player, game.getAvailableMagicians().get(choice));
    }

    /**
     * Method nextState determines the next game state after a ChooseMagician action is executed.
     * Only when all the players have selected a magician the game can proceed to the next state.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public GameState nextState(Game game, Rules rules) {
        if (game.getPlayers().indexOf(getPlayer(game)) == game.getPlayers().size() - 1) {
            // If the last player has selected a magician
            return GameState.PLANNING_CHOOSE_CARD;
        } else {
            // If players need to select a magician
            return GameState.SETUP_CHOOSE_MAGICIAN;
        }
    }

    /**
     * Method nextPlayer determines the next player after a ChooseMagician action is executed.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @return the next player for the planning phase, null if the owner is the last player.
     */
    @Override
    public Player nextPlayer(Game game, Rules rules) {
        // TODO HANDLE NEXT PLAYER NOT CONNECTED -> END GAME
        int currentPlayer = game.getPlayers().indexOf(getPlayer(game));
        if (currentPlayer == game.getPlayers().size() - 1) {
            // If it is the last player, meaning a change of state is necessary.
            return null;
        } else {
            // Returns the next player
            return game.getPlayers().get(currentPlayer + 1);
        }
    }
}