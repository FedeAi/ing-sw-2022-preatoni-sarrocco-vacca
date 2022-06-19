package it.polimi.ingsw.Client;

import java.io.*;
import java.util.*;
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
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.Answer;

public class ModelView implements Serializable {

    private transient Answer serverAnswer;
    private String playerName;
    private String roundOwner;
    private transient final CLI cli;
    private transient final GUI gui;

    /** Model data **/
    private List<String> connectedPlayers;
    private Map<Color, String> professors;
    private int balance;
    private Map<String, Magician> playerMapMagician;
    private Map<String, School> playerMapSchool;
    private List<AssistantCard> hand;
    private IslandContainer islandContainer;
    private List<Cloud> clouds;
    private Map<Magician, String>  mapMagicianPlayer;
    private List<ReducedCharacterCard> characterCards;
    private Map<String, AssistantCard> playedCards;
    private int motherNature;
    private GameState gameState, prevGameState;
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
        this.playerMapSchool = new HashMap <String, School>() ;
        this.connectedPlayers = new ArrayList<>();
        gameState = GameState.GAME_ROOM;
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

    public void setMagicians(Map<Magician, String> magicians){
        this.mapMagicianPlayer = magicians;
        playerMapMagician = mapMagicianPlayer.entrySet().stream().filter((e) -> !e.getValue().equals("")).collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public List<String> getAvailableMagiciansStr(){
        return mapMagicianPlayer.entrySet()
                .stream().filter(magicianStringEntry -> Objects.equals(magicianStringEntry.getValue(), "")).
                map(e -> e.getKey().toString()).toList();
    }

    /**
     *
     * @return map between magician and owner, the owner is "" if the prof is not owned by no one
     */
    public Map<Magician,String> getMagiciansToPlayer(){
        return new HashMap<>(mapMagicianPlayer);
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

    public GameState getPrevGameState(){
        return prevGameState;
    }

    public void setGameState(GameState gameState) {
        prevGameState = this.gameState;
        this.gameState = gameState;
    }

    public void setExpert(boolean expertMode) {
        expert = expertMode;
    }

    public boolean getExpert(){
        return expert;
    }

    public boolean amIRoundOwner(){
        return Objects.equals(roundOwner,playerName);
    }

    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        players.addAll(playerMapSchool.keySet());
        return players;
    }

//    public void saveToFile(){
//        try (FileOutputStream fos = new FileOutputStream("modelview.dat");
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//
//            // write object to file
//            oos.writeObject(this);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//    public ModelView readFromFile(){
//        ModelView modelView = null;
//        try (FileInputStream fis = new FileInputStream("modelview.dat");
//             ObjectInputStream ois = new ObjectInputStream(fis)) {
//
//            // read object from file
//            modelView = (ModelView) ois.readObject();
//
//        } catch (IOException | ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
//        return modelView;
//    }

}
