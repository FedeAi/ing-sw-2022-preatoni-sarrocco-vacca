package it.polimi.ingsw.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polimi.ingsw.Client.cli.CLI;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.ReducedCharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.Answer;

public class ModelView {

    private Answer serverAnswer;
    private String playerName;
    private String roundOwner;
    private final CLI cli;
    private final GUI gui;

    /** Model data **/
    private List<String> connectedPlayers;
    private Map<Color, String> professors;
    private int balance;
    private Map<String, Magician> playerMapMagician;
    private Map<String, School> playerMapSchool;
    private List<AssistantCard> hand;
    private IslandContainer islandContainer;
    private List<Cloud> clouds;
    private List<Magician> availableMagicians;
    private List<ReducedCharacterCard> characterCards;
    private Map<String, AssistantCard> playedCards;
    private int motherNature;
    private GameState gameState;
    private boolean expert = false;

    private boolean isInputActive = true; // TODO FIXME this must be set not true forever

    /**
     * Constructor ModelView creates a new ModelView instance.
     *
     * @param cli of type GUI - GUI reference.
     *
     **/

    public ModelView(CLI cli) {
        this.cli = cli;
        gui = null;
        this.playedCards = new HashMap<String, AssistantCard>();
        this.playerMapSchool = new HashMap <String, School>() ;
        this.connectedPlayers = new ArrayList<>();
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
        this.playerMapSchool = new HashMap <String, School>() ;
        this.connectedPlayers = new ArrayList<>();
    }
    /** Getter and Setter **/

    public Answer getServerAnswer() {
        return serverAnswer;
    }

    public void setServerAnswer(Answer serverAnswer) {
        this.serverAnswer = serverAnswer;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRoundOwner() {
        return roundOwner;
    }

    public void setRoundOwner(String roundOwner) {
        this.roundOwner = roundOwner;
    }

    public Map<Color, String> getProfessors() {
        return professors;
    }

    public void setProfessors(Map<Color, String> professors) {
        this.professors = professors;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Map<String, Magician> getPlayerMapMagician() {
        return playerMapMagician;
    }

    public void setPlayerMapMagician(Map<String, Magician> playerMapMagician) {
        this.playerMapMagician = playerMapMagician;
    }

    public Map<String, School> getPlayerMapSchool() {
        return playerMapSchool;
    }

    public void setPlayerSchool(String player, School school) {
        this.playerMapSchool.put(player, school);
    }

    public List<AssistantCard> getHand() {
        return hand;
    }

    public void setHand(List<AssistantCard> hand) {
        this.hand = hand;
    }

    public IslandContainer getIslandContainer() {
        return islandContainer;
    }

    public void setIslandContainer(IslandContainer islandContainer) {
        this.islandContainer = islandContainer;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public void setAvailableMagicians(List<Magician> availableMagicians){
        this.availableMagicians = availableMagicians;
    }

    public List<String> getAvailableMagiciansStr(){
        return availableMagicians.stream().map(Enum::toString).collect(Collectors.toList());
    }

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public List<ReducedCharacterCard> getCharacterCards() {
        return characterCards;
    }

    public void setCharacterCards(List<ReducedCharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

    public Map<String, AssistantCard> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCard(String player, AssistantCard playedCard) {
        this.playedCards.put(player, playedCard);
    }

    public void setMotherNature(int position){
        motherNature = position;
    }

    public void setConnectedPlayers(List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public int getMotherNature(){
        return motherNature;
    }
    public boolean isInputActive() {
        return isInputActive;
    }
    public void activateInput() {
        isInputActive = true;
    }
    public void deactivateInput() {
        isInputActive = false;
    }



    public CLI getCli(){
        return cli;
    }
    public GUI getGui( ) {
        return gui;
    }

    public GameState getGameState(){
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setExpert(boolean expertMode) {
        expert = expertMode;
    }

    public boolean getExpert(){
        return expert;
    }



    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        players.addAll(playerMapSchool.keySet());
        return players;
    }


}
