package it.polimi.ingsw.Server.Answer.modelUpdate;

import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Model.Cloud;

import java.util.List;
import java.util.Map;

/**
 * MagicianMessage class is a ModelMessage used for sending infos about .... to the client.
 *
 * @author Federico Sarrocco Davide Preatoni
 * @see ModelMessage
 */
public class MagicianMessage implements ModelMessage {
  private final Map<Magician,String> message;

  /**
   * Constructor MoveMessage creates a new MoveMessage instance.
   *
   * @param clouds .................
   */
  public MagicianMessage(Map<Magician,String> magicians) {
    this.message = magicians;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public Map<Magician,String> getMessage() {
    return message;
  }
}
