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
  private final School message;
  private final String player;
  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param newSchool .................
   */
  public SchoolMessage(String player, School newSchool) {
    this.message = newSchool;
    this.player = player;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see Answer#getMessage()
   */
  @Override
  public School getMessage() {
    return message;
  }

  public String getPlayer(){return  player;}
}
