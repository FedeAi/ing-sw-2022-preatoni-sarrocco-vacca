package it.polimi.ingsw.Server.Answer;

import it.polimi.ingsw.Model.Cards.AssistantCard;

import java.util.List;

/**
 * MoveMessage class is an Answer used for sending infos about a move action to the client.
 *
 * @author GC30
 * @see Answer
 */
public class AssistantCardMessage implements Answer {
  private final List<AssistantCard> message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param island .................
   */
  public AssistantCardMessage(List<AssistantCard> assistantCards) {
    this.message = assistantCards;
  }

  /**
   * Method getMessage returns the message of this WorkerPlacement object.
   *
   * @return the message (type Object) of this WorkerPlacement object.
   * @see Answer#getMessage()
   */
  @Override
  public List<AssistantCard> getMessage() {
    return message;
  }
}
