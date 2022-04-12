package it.polimi.ingsw.Controller.Actions;

import it.polimi.ingsw.Controller.Rules.Rules;
import it.polimi.ingsw.Model.Game;

public interface Performable {

    /**
     * Can perform ext boolean.
     * Check if the player can really make the move.
     *
     * @param game  of type GameExt: the game
     * @param rules
     * @return the boolean
     */
    boolean canPerformExt(Game game, Rules rules);

    /**
     * Perform move method.
     *  @param game of type GameExt: the game
     * @param rules
     */
    void performMove(Game game, Rules rules);

    /**
     * Gets NickName player.
     *
     * @return of type int: the player's id.
     */
    String getNickNamePlayer();
}
