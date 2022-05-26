package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Constants.Color;

import java.util.EnumMap;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class ProfsMessage implements ModelMessage {
  private final EnumMap<Color, String> message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param profs .................
   */
  public ProfsMessage(EnumMap<Color, String> profs) {
    this.message = profs;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public EnumMap<Color, String> getMessage() {
    return message;
  }
}
