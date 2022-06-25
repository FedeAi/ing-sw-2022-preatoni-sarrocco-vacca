package it.polimi.ingsw.controller.actions;

import it.polimi.ingsw.controller.rules.Rules;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.Optional;

/**
 * ActivateCard class represent the cloud select game action.
 */
public class ChooseCloud extends Performable {

    private final int choice;

    /**
     * Constructor ChooseCloud creates the ChooseCloud instance, and sets the cloud selection by index.
     *
     * @param player the nickname of the action owner.
     * @param choice the index of the cloud selected.
     */
    public ChooseCloud(String player, int choice) {
        super(player);
        this.choice = choice;
    }

    /**
     * Method canPerform extends the Performable definition with the ChooseCloud specific checks.
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
        if (!game.getGameState().equals(GameState.ACTION_CHOOSE_CLOUD)) {
            throw new WrongStateException("final step of the action phase.");
        }
        // A cloud for every player
        int numCloud = game.numPlayers();
        List<Cloud> clouds = game.getClouds();

        if (choice >= numCloud || choice < 0 || clouds.get(choice).isEmpty()) {
            throw new InvalidIndexException("cloud", 0, numCloud - 1, choice);
        }
    }

    /**
     * Method performMove checks if an action is performable,
     * and only if successful it executes the action on the Game Model.
     * The cloud will be selected and its students will be moved to the Player's School entry.
     * If all the clouds are empty, they will be refilled.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        canPerform(game, rules);
        Player player = getPlayer(game);

        player.getSchool().addStudentsEntry(game.pickCloud(choice));
        // If it is the end of the turn
        if (nextPlayer(game, rules) == null) {
            // Refill of students empty clouds
            game.refillClouds();
        }
    }

    /**
     * Method nextState determines the next game state after a ChooseCloud action is executed.
     * Only when all the players have reached the end of their turn the game can proceed to the next planning phase.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    @Override
    public GameState nextState(Game game, Rules rules) {
        if (nextPlayer(game, rules) == null) { //if end Turn
            return GameState.PLANNING_CHOOSE_CARD;
        } else {
            // Next player turn -> Move students phase
            return GameState.ACTION_MOVE_STUDENTS;
        }
    }

    /**
     * Method nextPlayer determines the next player after a ChooseCloud action is executed.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @return the next player for the action phase, null if the owner is the last player.
     */
    @Override
    public Player nextPlayer(Game game, Rules rules) {
        Optional<String> nextActionPlayer = game.getNextPlayerActionPhase();
        Player nextPlayer;
        int i = 1;

        while (nextActionPlayer.isPresent() && !game.getPlayerByNickname(nextActionPlayer.get()).get().isConnected()) {
            i++;
            nextActionPlayer = game.getNextPlayerActionPhase(i);
        }
        if (nextActionPlayer.isEmpty()) {
            // If it is the end of the turn
            nextPlayer = null;
        } else {
            Optional<Player> nextPlayerAction = game.getPlayerByNickname(nextActionPlayer.get());
            nextPlayer = nextPlayerAction.get();
        }
        return nextPlayer;
    }
}