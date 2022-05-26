package it.polimi.ingsw.Server.Answer.modelUpdate;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class BalanceMessage implements ModelMessage {
  private final int message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param clouds .................
   */
  public BalanceMessage(int balance) {
    this.message = balance;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public Integer getMessage() {
    return message;
  }
}
