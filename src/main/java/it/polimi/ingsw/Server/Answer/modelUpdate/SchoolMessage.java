package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Model.School;

/**
 * MoveMessage class is a ModelMessage used for sending infos about a move action to the client.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class SchoolMessage implements ModelMessage {
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
   * @see ModelMessage#getMessage()
   */
  @Override
  public School getMessage() {
    return message;
  }

  public String getPlayer(){return  player;}
}
