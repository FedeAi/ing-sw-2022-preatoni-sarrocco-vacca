package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.ReducedCharacterCard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HandMessage class is a ModelMessage used for sending infos about the new client's hand.
 *
 * @author Federico Sarrocco, Alessandro Vacca
 * @see ModelMessage
 */
public class CharactersMessage implements ModelMessage {
  private final List<ReducedCharacterCard> message;

  /**
   * Constructor HandMessage creates a new HandMessage instance.
   *
   * @param assistantCards .................
   */
  public CharactersMessage(List<CharacterCard> characterCards) {
    this.message = characterCards.stream().map(c ->
            new ReducedCharacterCard(c.isActive(), c.getClass().getSimpleName())
    ).toList();
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public List<ReducedCharacterCard> getMessage() {
    return message;
  }
}
