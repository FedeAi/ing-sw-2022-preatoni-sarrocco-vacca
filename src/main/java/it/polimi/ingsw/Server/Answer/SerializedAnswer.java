package it.polimi.ingsw.Server.Answer;

import java.io.Serializable;

/**
 * Class SerializedAnswer is a Answer that can be serialized.
 *
 * @author GC30
 * @see Serializable
 */
public class SerializedAnswer implements Serializable {
  private Answer serverAnswer;

  /**
   * Method setServerAnswer sets the serverAnswer of this SerializedAnswer object.
   *
   * @param answer the serverAnswer of this SerializedAnswer object.
   */
  public void setServerAnswer(Answer answer) {
    serverAnswer = answer;
  }

  /**
   * Method getServerAnswer returns the serverAnswer of this SerializedAnswer object.
   *
   * @return the serverAnswer (type Answer) of this SerializedAnswer object.
   */
  public Answer getServerAnswer() {
    return serverAnswer;
  }
}
