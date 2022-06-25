package it.polimi.ingsw.server.answers.model;

/**
 * BalanceMessage class is type of ModelMessage used for sending updates of the player's balance.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class BalanceMessage implements ModelMessage {

  private final int message;

  /**
   * Constructor BalanceMessage creates a new BalanceMessage instance.
   *
   * @param balance the player's updated balance.
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
