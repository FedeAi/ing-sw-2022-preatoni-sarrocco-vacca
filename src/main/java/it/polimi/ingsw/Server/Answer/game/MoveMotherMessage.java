package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Server.Answer.Answer;

/**
 * MoveMessage class is an Answer used for sending infos about a move action to the client.
 *
 * @author GC30
 * @see Answer
 */
public class MoveMotherMessage implements Answer {
  private final int island;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param island .................
   */
  public MoveMotherMessage(int island) {
    this.island = island;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see Answer#getMessage()
   */
  @Override
  public Integer getMessage() {
    return island;
  }
}
