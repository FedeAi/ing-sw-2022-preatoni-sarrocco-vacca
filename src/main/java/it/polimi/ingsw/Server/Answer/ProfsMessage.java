package it.polimi.ingsw.Server.Answer;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.EnumMap;

/**
 * MoveMessage class is an Answer used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see Answer
 */
public class ProfsMessage implements Answer {
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
   * Method getMessage returns the message of this WorkerPlacement object.
   *
   * @return the message (type Object) of this WorkerPlacement object.
   * @see Answer#getMessage()
   */
  @Override
  public EnumMap<Color, String> getMessage() {
    return message;
  }
}
