package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;

import java.util.List;

/**
 * HandMessage class is a ModelMessage used for sending infos about the new client's hand.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see ModelMessage
 */
public class CharactersMessage implements ModelMessage {
  private final List<CharacterCard> message;

  /**
   * Constructor HandMessage creates a new HandMessage instance.
   *
   * @param assistantCards .................
   */
  public CharactersMessage(List<CharacterCard> characterCards) {
    this.message = characterCards;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public List<CharacterCard> getMessage() {
    return message;
  }
}
