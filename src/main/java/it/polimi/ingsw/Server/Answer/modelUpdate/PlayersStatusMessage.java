package it.polimi.ingsw.Server.Answer.modelUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HandMessage class is a ModelMessage used for sending infos about the new client's hand.
 *
 * @author Alessandro Vacca
 * @see ModelMessage
 */
public class PlayersStatusMessage implements ModelMessage {
  public static final String CONNECTED_PLAYERS = "connected";
  public static final String PLAYERS = "players";
  public static final String REJOINING_PLAYERS = "rejoining";
  private final List<String> players;
  private List<String> connectedPlayers;
  private List<String> rejoiningPlayers;

  /**
   * Constructor PlayersStatusMessage creates a new ConnectedPlayers message instance.
   *
   * @param message map with keys PlayersStatusMessage.CONNECTED_PLAYERS PlayersStatusMessage.PLAYERS
   */
  public PlayersStatusMessage(Map<String, List<String>> message) {
    players = message.getOrDefault(PLAYERS, new ArrayList<>());
    connectedPlayers = message.getOrDefault(CONNECTED_PLAYERS, new ArrayList<>());
    rejoiningPlayers = message.getOrDefault(REJOINING_PLAYERS, new ArrayList<>());
  }


  public List<String> getConnectedPlayers(){
    return connectedPlayers;
  }

  public List<String> getPlayers(){
    return players;
  }
  public List<String> getRejoiningPlayers(){
    return rejoiningPlayers;
  }

  /**
   * Method getMessage returns the message of this Answer object.
   *
   * @return the message (type Object) of this Answer object.
   * @see ModelMessage#getMessage()
   */
  @Override
  public List<String> getMessage() {
    return players;
  }
}
