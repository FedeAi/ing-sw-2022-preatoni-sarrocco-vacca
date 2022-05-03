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
public class CloudsMessage implements Answer {
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
   * @see Answer#getMessage()
   */
  @Override
  public List<Cloud> getMessage() {
    return message;
  }
}
