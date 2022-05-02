package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Server.Answer.Answer;

import java.util.List;

/**
 * MoveMessage class is an Answer used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see Answer
 */
public class BalanceMessage implements Answer {
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
   * Method getMessage returns the message of this WorkerPlacement object.
   *
   * @return the message (type Object) of this WorkerPlacement object.
   * @see Answer#getMessage()
   */
  @Override
  public Integer getMessage() {
    return message;
  }
}
