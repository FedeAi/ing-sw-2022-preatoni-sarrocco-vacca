package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.Cards.AssistantCard;

import java.util.List;

/**
 * HandMessage class is a ModelMessage used for sending infos about the new client's hand.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see ModelMessage
 */
public class HandMessage implements ModelMessage {
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
   * @see ModelMessage#getMessage()
   */
  @Override
  public List<AssistantCard> getMessage() {
    return message;
  }
}
