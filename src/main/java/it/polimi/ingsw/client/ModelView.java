package it.polimi.ingsw.client;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.constants.Magician;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.characters.ReducedCharacterCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.islands.IslandContainer;
import it.polimi.ingsw.model.School;
import it.polimi.ingsw.server.answers.Answer;

/**
 * ModelView class represents the client's data for the View implementation.
 */
public class ModelView implements Serializable {

    private transient Answer serverAnswer;
    private String playerName;
    private String roundOwner;
    private transient final CLI cli;
    private transient final GUI gui;

    /**
     * Model data
     **/
    private List<String> connectedPlayers;
    private List<String> players;
    private Map<Color, String> professors;
    private int balance;
    private Map<String, Magician> playerMapMagician;
    private Map<String, School> playerMapSchool;
    private List<AssistantCard> hand;
    private IslandContainer islandContainer;
    private List<Cloud> clouds;
    private Map<Magician, String> mapMagicianPlayer;
    private List<ReducedCharacterCard> characterCards;
    private Map<String, AssistantCard> playedCards;
    private int motherNature;
    private GameState gameState, prevGameState;
    private boolean expert = false;

    private boolean isInputActive = true; // TODO FIXME this must be set not true forever

    /**
     * Constructor ModelView creates a new ModelView instance.
     *
     * @param cli of type CLI - CLI reference.
     **/
    public ModelView(CLI cli) {
        this.cli = cli;
        gui = null;
        this.playedCards = new HashMap<String, AssistantCard>();
        this.playerMapSchool = new HashMap<String, School>();
        this.connectedPlayers = new ArrayList<>();
        gameState = GameState.GAME_ROOM;
    }

    /**
     * Constructor ModelView creates a new ModelView instance.
     *
     * @param gui of type GUI - GUI reference.
     */
    public ModelView(GUI gui) {
        this.gui = gui;
        this.cli = null;
        this.playedCards = new HashMap<String, AssistantCard>();
        this.playerMapSchool = new HashMap<String, School>();
        this.connectedPlayers = new ArrayList<>();
        gameState = GameState.GAME_ROOM;
    }

    /**
     * Method getServerAnswer returns the current Answer received from the Server.
     **/
    public Answer getServerAnswer() {
        return serverAnswer;
    }

    /**
     * Method setServerAnswer sets the current Answer received from the Server.
     *
     * @param serverAnswer the Answer to be set.
     */
    public void setServerAnswer(Answer serverAnswer) {
        this.serverAnswer = serverAnswer;
    }

    /**
     * Method getPlayerName returns the client's player name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Method setPlayerName sets the client's player name.
     *
     * @param playerName the name to be set.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Method getRoundOwner returns the current roundOwner.
     *
     * @return the name of the current roundOwner.
     */
    public String getRoundOwner() {
        return roundOwner;
    }

    /**
     * Method setRoundOwner sets the current roundOwner.
     *
     * @param roundOwner the roundOwner to be set.
     */
    public void setRoundOwner(String roundOwner) {
        this.roundOwner = roundOwner;
    }

    /**
     * Method getProfessors returns the current professor owners.
     *
     * @return a map of professors and names.
     */
    public Map<Color, String> getProfessors() {
        return professors;
    }

    /**
     * Method setProfessors sets the current professor owners.
     *
     * @param professors the map of professors and names.
     */
    public void setProfessors(Map<Color, String> professors) {
        this.professors = professors;
    }

    /**
     * Method getBalance returns the current player's balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Method getBalance sets the current player's balance.
     *
     * @param balance the balance to be set.
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Method getPlayerMapMagician returns a map of player nicknames to magicians.
     */
    public Map<String, Magician> getPlayerMapMagician() {
        return playerMapMagician;
    }

    /**
     * Method getPlayerMapMagician returns a map of player nicknames to school.
     */
    public Map<String, School> getPlayerMapSchool() {
        return playerMapSchool;
    }

    /**
     * Method setPlayerSchool inserts into the playerMapSchool a new school and owner.
     *
     * @param player the school's owner.
     * @param school the school reference.
     */
    public void setPlayerSchool(String player, School school) {
        this.playerMapSchool.put(player, school);
    }

    /**
     * Method getHand returns the current player's hand.
     */
    public List<AssistantCard> getHand() {
        return hand;
    }

    /**
     * Method setHand sets the current player's hand.
     *
     * @param hand the list of cards (hand) to be set.
     */
    public void setHand(List<AssistantCard> hand) {
        this.hand = hand;
    }

    /**
     * Method getIslandContainer returns the current IslandContainer.
     */
    public IslandContainer getIslandContainer() {
        return islandContainer;
    }

    /**
     * Method setIslandContainer sets the updated IslandContainer.
     *
     * @param islandContainer the updated IslandContainer.
     */
    public void setIslandContainer(IslandContainer islandContainer) {
        this.islandContainer = islandContainer;
    }

    /**
     * Method getClouds returns the current game's clouds.
     *
     * @return the current List of clouds.
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Method setMagicians sets the updated player - magician map.
     *
     * @param magicians the updated player - magician map.
     */
    public void setMagicians(Map<Magician, String> magicians) {
        this.mapMagicianPlayer = magicians;
        playerMapMagician = mapMagicianPlayer.entrySet().stream().filter((e) -> !e.getValue().equals("")).collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * Method getAvailableMagiciansStr returns the available Magicians.
     *
     * @return The list of names of available magicians.
     */
    public List<String> getAvailableMagiciansStr() {
        return mapMagicianPlayer.entrySet()
                .stream().filter(magicianStringEntry -> Objects.equals(magicianStringEntry.getValue(), "")).
                map(e -> e.getKey().toString()).toList();
    }

    /**
     * Method getAvailableMagicians returns the available Magicians.
     *
     * @return The list of available magicians.
     */
    public List<Magician> getAvailableMagicians() {
        return Magician.orderMagicians(mapMagicianPlayer.entrySet()
                .stream().filter(magicianStringEntry -> Objects.equals(magicianStringEntry.getValue(), "")).
                map(e -> e.getKey()).toList());
    }

    /**
     * Method getMagiciansToPlayer returns the updated magician - player map.
     *
     * @return The updated magician - player map.
     */
    public Map<Magician, String> getMagiciansToPlayer() {
        return new HashMap<>(mapMagicianPlayer);
    }

    /**
     * Method setClouds sets the updated game's clouds.
     *
     * @param clouds the updated cloud list.
     */
    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    /**
     * Method getCharacterCards returns the current game's character cards.
     *
     * @return The current game's character cards.
     */
    public List<ReducedCharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Method getCharacterCards sets the updated game's character cards.
     *
     * @param characterCards the updated list of character cards.
     */
    public void setCharacterCards(List<ReducedCharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

    /**
     * Method getPlayedCards returns the currently played cards.
     *
     * @return The nickname - card Map, that represents the currently played cards and who has played them.
     */
    public Map<String, AssistantCard> getPlayedCards() {
        return playedCards;
    }

    /**
     * Method setPlayedCard sets a new played cards.
     *
     * @param player     the name of the player that has played card.
     * @param playedCard the reference to the played card.
     */
    public void setPlayedCard(String player, AssistantCard playedCard) {
        if (playedCard == null) {
            this.playedCards.remove(player);
        } else {
            this.playedCards.put(player, playedCard);
        }
    }

    /**
     * Method setMotherNature sets the updated motherNature position.
     *
     * @param position MotherNature's new position.
     */
    public void setMotherNature(int position) {
        motherNature = position;
    }

    /**
     * Method setConnectedPlayers sets the updated list of currently connected players.
     *
     * @param connectedPlayers the updated list of connected players.
     */
    public void setConnectedPlayers(List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    /**
     * Method setPlayers sets the current game's players.
     *
     * @param players the current game's players.
     */
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * Method getPlayers returns the current game's players.
     *
     * @return The list of the current game's players.
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Method getConnectedPlayers returns the currently connected players.
     *
     * @return The list of the currently connected players.
     */
    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    /**
     * Method getMotherNature returns the current motherNature position.
     *
     * @return the current motherNature position.
     */
    public int getMotherNature() {
        return motherNature;
    }

    /**
     * Method isInputActive returns if a player can give input at the time.
     *
     * @return true if a player can give input, false otherwise.
     */
    public boolean isInputActive() {
        return isInputActive;
    }

    /**
     * Method activateInput sets the isInputActive status to true.
     */
    public void activateInput() {
        isInputActive = true;
    }

    /**
     * Method activateInput sets the isInputActive status to false.
     */
    public void deactivateInput() {
        isInputActive = false;
    }

    /**
     * Method getCli returns the reference to the current client CLI.
     *
     * @return The current client CLI's reference.
     */
    public CLI getCli() {
        return cli;
    }

    /**
     * Method getCli returns the reference to the current client GUI.
     *
     * @return The current client GUI's reference.
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * Method getGameState returns the current GameState.
     *
     * @return The current GameState.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Method getPrevGameState returns the previous GameState.
     *
     * @return The previous GameState.
     */
    public GameState getPrevGameState() {
        return prevGameState;
    }

    /**
     * Method setGameStates sets the updated GameState.
     * @param gameState the updated GameState.
     */
    public void setGameState(GameState gameState) {
        prevGameState = this.gameState;
        this.gameState = gameState;
    }

    /**
     * Method setExpert sets the game mode.
     * @param expertMode the flag of the game mode. True for expertMode, false otherwise.
     */
    public void setExpert(boolean expertMode) {
        expert = expertMode;
    }

    /**
     * Method getExpert returns is a game is set to the expert mode.
     * @return True if a game is set to the expert mode, false otherwise.
     */
    public boolean getExpert() {
        return expert;
    }

    /**
     * Method amIRoundOwner returns if the player is the current round owner.
     * @return True if the player is the current round owner, false otherwise.
     */
    public boolean amIRoundOwner() {
        return Objects.equals(roundOwner, playerName);
    }

    /**
     * Method clear regenerates the modelView instance after the game end.
     */
    public void clear() {
        this.playedCards = new HashMap<String, AssistantCard>();
        this.playerMapSchool = new HashMap<String, School>();
        this.connectedPlayers = new ArrayList<>();
        playerName = null;
        roundOwner = null;
        professors = new HashMap<>();
        playerMapMagician = new HashMap<>();
        mapMagicianPlayer = new HashMap<>();
        expert = false;
        gameState = GameState.GAME_ROOM;
    }
}