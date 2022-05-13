package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.Cloud;

import java.util.List;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class CloudsMessage implements ModelMessage {
  private final List<Cloud> message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param clouds .................
   */
  public CloudsMessage(List<Cloud> clouds) {
    this.message = clouds;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public List<Cloud> getMessage() {
    return message;
  }
}
