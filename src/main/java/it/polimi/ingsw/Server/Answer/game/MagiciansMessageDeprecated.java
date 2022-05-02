package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Server.Answer.Answer;

import java.util.List;

/**
 * MoveMessage class is an Answer used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see Answer
 */
public class MagiciansMessageDeprecated implements Answer {
  private final List<Magician> message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param magicians .................
   */
  public MagiciansMessageDeprecated(List<Magician> magicians) {
    this.message = magicians;
  }

  /**
   * Method getMessage returns the message of this WorkerPlacement object.
   *
   * @return the message (type Object) of this WorkerPlacement object.
   * @see Answer#getMessage()
   */
  @Override
  public List<Magician> getMessage() {
    return message;
  }
}
