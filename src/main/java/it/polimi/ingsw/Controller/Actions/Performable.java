package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public abstract class Performable {
    protected String myNickName;

    public Performable(String nickName){
        this.myNickName = nickName;
    }

    /**
     * Can perform ext boolean.
     * Check if the player can really make the move.
     *
     * @param game  of type GameExt: the game
     * @param rules
     * @return the boolean
     */
    protected boolean canPerformExt(Game game, Rules rules){
        Optional<Player> player_opt = game.getPlayerByNickname(myNickName);
        if (player_opt.isEmpty())    // if there is no Player with that nick
            return false;
        Player player = player_opt.get();

        if (!game.getRoundOwner().equals(player)) {   // if the player is not the round owner
            return false;
        }
        return true;
    }

    /**
     * Perform move method.
     *  @param game of type GameExt: the game
     * @param rules
     */
    protected abstract void performMove(Game game, Rules rules);

    /**
     * Gets NickName player.
     *
     * @return of type int: the player's id.
     */
    String getNickNamePlayer(){
        return myNickName;
    }

    /**
     * this method return the player given the nickname
     *
     * @param game
     * @return
     */
    protected Player getPlayer(Game game){
        Optional<Player> playerByNickname = game.getPlayerByNickname(myNickName);
        return playerByNickname.orElse(null);
    }

}
