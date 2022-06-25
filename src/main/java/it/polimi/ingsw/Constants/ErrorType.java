package it.polimi.ingsw.Constants;

/**
 * Class GameState represents all the possibile errors sent by the server.
 *
 * @see it.polimi.ingsw.Server.answers.GameError
 */
public enum ErrorType {
    DUPLICATE_NICKNAME,
    NOT_YOUR_TURN,
    INVALID_NICKNAME,
    INVALID_MOVE,
    FULL_LOBBY
}
