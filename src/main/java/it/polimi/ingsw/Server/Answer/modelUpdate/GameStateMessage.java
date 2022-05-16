package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Constants.GameState;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author FedericoSarrocco
 * @see ModelMessage
 */
public class GameStateMessage implements ModelMessage {
  private final GameState message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param message .................
   */
  public GameStateMessage(GameState message) {
    this.message =message ;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public GameState getMessage() {
    return message;
  }
}
