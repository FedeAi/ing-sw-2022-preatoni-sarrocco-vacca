package it.polimi.ingsw.Server.Answer.modelUpdate;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author GC30
 * @see ModelMessage
 */
public class MoveMotherMessage implements ModelMessage {
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
   * @see ModelMessage#getMessage()
   */
  @Override
  public Integer getMessage() {
    return island;
  }
}
