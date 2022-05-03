package it.polimi.ingsw.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.Client.cli.CLI;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cards.CharacterCards.CharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.Answer;

public class ModelView {

    private Answer serverAnswer;
    private String playerName;
    private String roundOwner;
    private final CLI cli;
    private final GUI gui;

    /** Model data **/
    private Map<Color, String> professors;
    private int balance;
    private Map<String, Magician> playerMapMagician;
    private Map<String, School> playerMapSchool;
    private List<AssistantCard> hand;
    private IslandContainer islandContainer;
    private List<Cloud> clouds;
    private List<CharacterCard> characterCards;
    private Map<String, AssistantCard> playedCards;

    /**
     * Constructor ModelView creates a new ModelView instance.
     *
     * @param cli of type GUI - GUI reference.
     */

    public ModelView(CLI cli) {
        this.cli = cli;
        gui = null;
        this.playedCards = new HashMap<String, AssistantCard>();
        this.playerMapSchool = new HashMap <String, School>() ;

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

    public void setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public void setCharacterCards(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

    public Map<String, AssistantCard> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(String player, AssistantCard playedCard) {
        this.playedCards.put(player, playedCard);
    }

    public CLI getCli(){
        return cli;
    }
    public GUI getGui( ) {
        return gui;
    }
}
