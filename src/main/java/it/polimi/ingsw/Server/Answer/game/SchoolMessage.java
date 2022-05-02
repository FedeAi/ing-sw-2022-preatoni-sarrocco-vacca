package it.polimi.ingsw.Server.Answer.game;

import it.polimi.ingsw.Constants.Pair;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.Answer;

/**
 * MoveMessage class is an Answer used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see Answer
 */
public class SchoolMessage implements Answer {
  private final Pair<String, School> message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param newSchool .................
   */
  public SchoolMessage(Pair<String, School> newSchool) {
    this.message = newSchool;
  }

  /**
   * Method getMessage returns the message of this WorkerPlacement object.
   *
   * @return the message (type Object) of this WorkerPlacement object.
   * @see Answer#getMessage()
   */
  @Override
  public Pair<String, School> getMessage() {
    return message;
  }
}
