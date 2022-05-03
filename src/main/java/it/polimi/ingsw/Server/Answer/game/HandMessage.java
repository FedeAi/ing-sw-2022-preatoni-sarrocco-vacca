package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Server.Answer.Answer;

import java.util.List;

/**
 * HandMessage class is an Answer used for sending infos about the new client's hand.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see Answer
 */
public class HandMessage implements Answer {
  private final List<AssistantCard> message;

  /**
   * Constructor HandMessage creates a new HandMessage instance.
   *
   * @param assistantCards .................
   */
  public HandMessage(List<AssistantCard> assistantCards) {
    this.message = assistantCards;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see Answer#getMessage()
   */
  @Override
  public List<AssistantCard> getMessage() {
    return message;
  }
}
