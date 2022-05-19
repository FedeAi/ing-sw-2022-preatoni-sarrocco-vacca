package it.polimi.ingsw.Server.Answer.modelUpdate;

/**
 * ModeMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @see ModelMessage
 */
public class ModeMessage implements ModelMessage {
  private final Boolean message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param isExpert .................
   */
  public ModeMessage(Boolean isExpert) {
    this.message = isExpert;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   * It returns the match mode
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public Boolean getMessage() {
    return message;
  }
}
