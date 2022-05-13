package it.polimi.ingsw.Server.Answer.modelUpdate;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author Federico Sarrocco
 * @see ModelMessage
 */

public class RoundOwnerMessage implements ModelMessage {
  private final String message;

  /**
   * Constructor RoundOwnerMessage creates a new RoundOwnerMessage instance.
   *
   * @param turnOwner .................
   */
  public RoundOwnerMessage(String turnOwner) {
    this.message = turnOwner;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public String getMessage() {
    return message;
  }
}
