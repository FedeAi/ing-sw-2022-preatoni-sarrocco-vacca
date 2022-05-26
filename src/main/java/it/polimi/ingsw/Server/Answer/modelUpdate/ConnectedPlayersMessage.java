package it.polimi.ingsw.Server.Answer.modelUpdate;

import java.util.List;

/**
 * HandMessage class is a ModelMessage used for sending infos about the new client's hand.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class ConnectedPlayersMessage implements ModelMessage {
  private final List<String> message;

  /**
   * Constructor HandMessage creates a new HandMessage instance.
   *
   * @param message the list of the current connected players.
   */
  public ConnectedPlayersMessage(List<String> message) {
    this.message = message;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public List<String> getMessage() {
    return message;
  }
}
