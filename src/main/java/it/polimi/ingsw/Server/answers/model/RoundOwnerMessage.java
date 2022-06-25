package it.polimi.ingsw.Server.answers.model;

/**
 * RoundOwnerMessage class is a ModelMessage used for sending updates of the current round owner.
 *
 * @author Federico Sarrocco, Alessandro Vacca.
 * @see ModelMessage
 */
public class RoundOwnerMessage implements ModelMessage {

  private final String message;

  /**
   * Constructor RoundOwnerMessage creates a new RoundOwnerMessage instance.
   *
   * @param roundOwner the new round owner.
   */
  public RoundOwnerMessage(String roundOwner) {
    this.message = roundOwner;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public String getMessage() {
    return message;
  }
}