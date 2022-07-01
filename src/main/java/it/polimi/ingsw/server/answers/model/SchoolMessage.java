package it.polimi.ingsw.server.answers.model;

import it.polimi.ingsw.model.School;

/**
 * SchoolMessage class is a ModelMessage used for sending updates of the players' School.
 *
 * @see ModelMessage
 */
public class SchoolMessage implements ModelMessage {

  private final School message;
  private final String player;

  /**
   * Constructor SchoolMessage creates a new SchoolMessage instance.
   *
   * @param newSchool the updated player's school.
   * @param player the name of the school owner.
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

  /**
   * Method getPlayer returns the current School owner.
   */
  public String getPlayer(){return  player;}
}