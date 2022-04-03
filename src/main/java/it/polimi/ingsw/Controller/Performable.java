package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;

public interface Performable {
    /**
     * Perform move method.
     *
     * @param game of type GameExt: the game
     */
    void performMove(Game game);

    /**
     * Can perform ext boolean.
     * Check if the player can really make the move.
     * @param game of type GameExt: the game
     * @return the boolean
     */
    boolean canPerformExt(Game game);

    /**
     * Gets NickName player.
     *
     * @return of type int: the player's id.
     */
    String getNickNamePlayer();
}
