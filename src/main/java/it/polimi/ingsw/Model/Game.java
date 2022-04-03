package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;
import it.polimi.ingsw.Model.Enumerations.GameState;
import it.polimi.ingsw.Model.Enumerations.Magician;

import java.util.*;

public class Game {

    static final int maxPlayers = 4;
    static final int maxIslands = 12;

    private static Game instance;

    private List<Player> players;
    private List<Player> playersActionPhase; // Ordered list of players for the action phase TODO ADD UML

    private List<Magician> magicians;
    private boolean expertMode;

    private MotherNature motherNature;
    private List<Island> islands;
    private List<Cloud> clouds;
    private EnumMap<Color, Integer> professors;

    private List<CharacterCard> characterCards; //array, fixed size = 3
    private GameState gameState;
    private Player roundOwner; // TODO add uml


    /**
     * The singleton instance of the game returns, if it has not been created it allocates it as well
     *
     * @return the singleton instance
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * @return list of Cards played until round owner
     */
    public List<AssistantCard> getPlayedCards() {
        ArrayList<AssistantCard> playedCards = new ArrayList<AssistantCard>();
        for (Player p : getOrderedPlanningPlayers()) {
            if (!p.equals(roundOwner)) {
                playedCards.add(p.getPlayedCard());
            } else {
                break;
            }
        }
        return playedCards;
    }

    /**
     * @return ordered players List for the next planning phase
     */
    public List<Player> getOrderedPlanningPlayers() {
        ArrayList<Player> orderedPlanningPLayers = new ArrayList<Player>();
        int playerIndex = players.indexOf(playersActionPhase.get(0));
        for (int i = 0; i < players.size(); i++) {
            orderedPlanningPLayers.add(players.get((playerIndex + i) % players.size()));
        }
        return orderedPlanningPLayers;
    }

    public String getPlayerNickname(Player player) { //ottengo il nickname
        return player.getNickname();
    }

    public List<Magician> getAvailableMagicians() { //choice for available magicians; main character
        return magicians;
    }

    public void removeMagician(Magician magician) { //delete the magicians from the possible choice
        magicians.remove(magician);

    }

    public boolean addPlayer(Player player) { //adding the player if the name isn't already taken
        boolean flag = false;
        if (!(isNicknameTaken(player.getNickname()))) {
            players.add(player);
            flag = true;
        }
        return flag;

    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getRoundOwner() {
        return roundOwner;
    }

    public void setRoundOwner(Player roundOwner) {
        this.roundOwner = roundOwner;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayersActionPhase(List<Player> playersActionPhase) {
        this.playersActionPhase = playersActionPhase;
    }

    public Optional<Player> getPlayerByNickname(String name) { //getting the object player by nickname
        Player player = null;
        for (Player p : players) {
            if (p.getNickname().equals(name)) {
                player = p;
            }
        }
        return Optional.ofNullable(player);
    }

    private boolean isNicknameTaken(String name) { //check if the name is available
        boolean flag = true;
        for (Player p : players) {
            if (p.getNickname().equals(name)) {
                flag = false;
            }
        }
        return flag;

    }

    public EnumMap<Color, Integer> getProfessors() {
        return professors;
    }

    public void setProfessors(EnumMap<Color, Integer> professors) {
        this.professors = professors;
    }
}
