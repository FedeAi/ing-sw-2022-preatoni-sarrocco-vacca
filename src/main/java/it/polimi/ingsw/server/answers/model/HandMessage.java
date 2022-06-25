package it.polimi.ingsw.server.answers.model;

import it.polimi.ingsw.model.cards.AssistantCard;

import java.util.List;

/**
 * HandMessage class is type of ModelMessage used for sending updates of player's hand.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see ModelMessage
 */
public class HandMessage implements ModelMessage {
  private final List<AssistantCard> message;

  /**
   * Constructor HandMessage creates a new HandMessage instance.
   *
   * @param assistantCards the player's assistant card list.
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
