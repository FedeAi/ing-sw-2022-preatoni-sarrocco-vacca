package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Exceptions.GameException;
import it.polimi.ingsw.Exceptions.InvalidPlayerException;
import it.polimi.ingsw.Exceptions.RoundOwnerException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public abstract class Performable {
    protected String myNickName;

    public Performable(String nickName) {
        this.myNickName = nickName;
    }

    /**
     * CanPerform method checks if a player can perform a move.
     *
     * @param game - represents the game Model.
     * @param rules - represents the current game rules.
     * @throws InvalidPlayerException
     * @throws RoundOwnerException
     */
    protected void canPerform(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException {
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        // Checks if there is a player with the specified nickname in the game
        if (player_opt.isEmpty()) {
            throw new InvalidPlayerException();
        }
        Player player = player_opt.get();

        // Checks if the player is the round owner
        if (!player.equals(game.getRoundOwner())) {
            throw new RoundOwnerException(game.getRoundOwner().getNickname());
        }
    }

    /**
     * Perform move method.
     *
     * @param game  of type GameExt: the game
     * @param rules
     */
    public abstract void performMove(Game game, Rules rules) throws InvalidPlayerException, RoundOwnerException, GameException;

    public GameState nextState(Game game, Rules rules){
        return game.getGameState();
    }

    public Player nextPlayer(Game game, Rules rules){
        return game.getRoundOwner();
    }

    /**
     * Gets NickName player.
     *
     * @return of type int: the player's id.
     */
    String getNickNamePlayer() {
        return myNickName;
    }

    /**
     * this method return the player given the nickname
     *
     * @param game
     * @return
     */
    protected Player getPlayer(Game game) {
        Optional<Player> playerByNickname = game.getPlayerByNickname(myNickName);
        return playerByNickname.orElse(null);
    }

}
