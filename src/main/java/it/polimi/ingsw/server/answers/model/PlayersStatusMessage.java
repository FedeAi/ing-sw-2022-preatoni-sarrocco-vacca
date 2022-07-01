package it.polimi.ingsw.server.answers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PlayersStatusMessage class is a ModelMessage used for sending updates of the players' connection status.
 *
 * @see ModelMessage
 */
public class PlayersStatusMessage implements ModelMessage {

    public static final String CONNECTED_PLAYERS = "connected";
    public static final String PLAYERS = "players";
    public static final String REJOINING_PLAYERS = "rejoining";
    private final List<String> players;
    private List<String> activePlayers;
    private List<String> rejoiningPlayers;

    /**
     * Constructor PlayersStatusMessage creates a new PlayersStatusMessage instance.
     *
     * @param message map that specifies the current connection status. Allowed keys are
     *                PlayersStatusMessage.CONNECTED_PLAYERS, PlayersStatusMessage.PLAYERS ,
     *                PlayersStatusMessage.REJOINING_PLAYERS
     * @see PlayersStatusMessage
     */
    public PlayersStatusMessage(Map<String, List<String>> message) {
        players = message.getOrDefault(PLAYERS, new ArrayList<>());
        activePlayers = message.getOrDefault(CONNECTED_PLAYERS, new ArrayList<>());
        rejoiningPlayers = message.getOrDefault(REJOINING_PLAYERS, new ArrayList<>());
    }

    /**
     * Method getActivePlayers returns the current connected players.
     *
     * @return A list of connected players.
     */
    public List<String> getActivePlayers() {
        return activePlayers;
    }

    /**
     * Method getPlayers returns the current game's players.
     *
     * @return A list of players.
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Method getRejoiningPlayers returns the players that are re-entering the game.
     *
     * @return A list of re-entering players.
     */
    public List<String> getRejoiningPlayers() {
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