package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

/**
 * Performable class is an abstract shared between every action possible in the game.
 */
public abstract class Performable {
    protected String player;

    /**
     * Constructor Performable initializes the instance and sets the action owner.
     *
     * @param player the nickname of the action owner.
     */
    public Performable(String player) {
        this.player = player;
    }

    /**
     * CanPerform method checks if a player can perform a move, determined by his objects and game state.
     *
     * @param game  represents the game Model.
     * @param rules represents the current game rules.
     * @throws InvalidPlayerException if the player is not in the current game.
     * @throws RoundOwnerException    if the player is not the current round owner.
     * @throws GameException          if there is no round owner, or you are the only player left in the game (the game is frozen).
     */
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        Optional<Player> player_opt = game.getPlayerByNickname(player);
        // Checks if there is a player with the specified nickname in the game
        if (player_opt.isEmpty()) {
            throw new InvalidPlayerException();
        }
        Player player = player_opt.get();

        // Checks if the player is the round owner
        if (!player.equals(game.getRoundOwner())) {
            if (game.getRoundOwner() != null)
                throw new RoundOwnerException(game.getRoundOwner().getNickname());
            else
                throw new GameException("RoundOwner is empty (Internal server error).");
        }

        // Checks the number of active players, if I'm the only one solitario is a better game for me
        if (game.numActivePlayers() == 1) {
            throw new GameException("You are the only player, wait the others");
        }
    }

    /**
     * Method performMove will be overridden by each action to determine the action behaviour.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     * @see Performable#canPerform(Game, Rules)
     */
    public abstract void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException;

    /**
     * Method nextState will be overridden by each action to determine the next game state.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    public GameState nextState(Game game, Rules rules) {
        return game.getGameState();
    }

    /**
     * Method nextPlayer will be overridden by each action to determine the next player.
     *
     * @param game  the current game model reference.
     * @param rules the current game rules.
     */
    public Player nextPlayer(Game game, Rules rules) {
        return game.getRoundOwner();
    }

    /**
     * Method getNickname returns the nickname of the action's owner.
     */
    public String getNickname() {
        return player;
    }

    /**
     * Method getPlayer returns the Player reference to  the action's owner.
     *
     * @param game the current game model reference.
     * @return the reference to the player if present in the game, null otherwise.
     */
    protected Player getPlayer(Game game) {
        Optional<Player> playerByNickname = game.getPlayerByNickname(player);
        return playerByNickname.orElse(null);
    }
}