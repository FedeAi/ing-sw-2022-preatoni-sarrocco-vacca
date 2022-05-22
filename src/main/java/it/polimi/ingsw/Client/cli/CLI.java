package it.polimi.ingsw.Client.cli;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Constants.*;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import it.polimi.ingsw.Server.Answer.GameError;
import it.polimi.ingsw.Server.Answer.ReqPlayersMessage;
import it.polimi.ingsw.Server.Answer.modelUpdate.PlayedCardMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * ClI implements the UI and that is the command line interface of view concept
 *
 * @author Davide Preatoni, Federico Sarrocco, Alessandro Vacca
 */

public class CLI implements UI {

    private static final Logger logger = Logger.getLogger(CLI.class.getName());
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private final PrintStream output;
    private final Scanner input;
    private final ModelView modelView;
    private final ServerMessageHandler serverMessageHandler;
    private boolean activeGame;
    private final String SHOW_COMMANDS = "[SCHOOL/ACTIONS/BOARD/CLOUDS/CARDS/PROFS]";
    private final String SHOW_COMMANDS_EXPERT = "[SCHOOL/ACTIONS/BOARD/CLOUDS/CARDS/PROFS/BALANCE/CHARACTERCARDS]";

    private ConnectionSocket connectionSocket;
    /**
     * Constructor of the class
     */
    public CLI() {

        activeGame = true;
        input = new Scanner(System.in);
        modelView = new ModelView(this);
        output = new PrintStream(System.out);
        serverMessageHandler = new ServerMessageHandler(this, modelView);

    }
    /**
     * Runnable main
     */
    public static void main(String[] args) {

//       do {
            clearScreen(); //cleaning terminal
            System.out.println(Constants.ERIANTYS);
            System.out.println(Constants.AUTHORS);
            Scanner scanner = new Scanner(System.in);
        System.out.println(">Insert the server IP address");
        System.out.print(">");
        String ip = scanner.nextLine();
//        validateIp(ip);
        System.out.println(">Insert the server port");
        System.out.print(">");
        String port = scanner.nextLine();
//        validatePort(port);
//
//       }while(validatePort(port) == -1 || validateIp(ip) = false);
        Constants.setAddress(ip);
        Constants.setPort(Integer.parseInt(port));
        CLI cli = new CLI();
        cli.run();
    }
    /**
     * Run is a method that listen the input till the game is active
     */
    public void run() {
        setup();
        while (isActiveGame()) {
            input.reset();
            String cmd;
            while ((cmd = input.nextLine()).isEmpty()) {} //bug fixed for Linux: scanner take also 'enter' like a command

            // cli commands can be model-show or action commands, if it's not a model-show command it's an action command
            if(!handleView(cmd)){
                listeners.firePropertyChange("action", null, cmd); //if it's not a model-show command it's an action
            }
        }
        input.close();
        output.close();
    }
    /**
     * Setup ask the username and establishes the connection with the server
     */
    public void setup() {

        String nickname = null;
        boolean confirmation = false;

        while (!confirmation) {
            do {
                System.out.println(">Insert your nickname: ");
                System.out.print(">");
                nickname = input.nextLine();
            } while(!validateNickname(nickname));

//            System.out.println(">You chose: " + nickname);
//            System.out.println(">Is it ok? [y/n] ");
//            System.out.print(">");
//            if (input.nextLine().equalsIgnoreCase("y")) {
//                confirmation = true;
//            } else {
//                nickname = null;
//            }
            confirmation = true; // FIXME REMOVE JUST TEMP
        }
        connectionSocket = new ConnectionSocket();
        modelView.setPlayerName(nickname);
        try {
            if (!connectionSocket.setup(nickname, modelView, serverMessageHandler)) {
                System.err.println("The entered IP/port doesn't match any active server or the server is not " +
                        "running. Please try again!");
                CLI.main(null);
            }
            System.out.println(CLIColors.ANSI_GREEN + "Socket Connection setup completed!"+ CLIColors.RESET);
        } catch (DuplicateNicknameException | InvalidNicknameException e) {
            setup();
        }
        listeners.addPropertyChangeListener("action", new InputToMessage(modelView, connectionSocket));
    }
    /**
     * Proprierty changes of interest
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ServerMessageHandler.GAME_ERROR_LISTENER ->
                    System.out.println(CLIColors.ANSI_RED + ((GameError) evt.getNewValue()).getMessage() + CLIColors.RESET);
            case ServerMessageHandler.REQ_PLAYERS_LISTENER ->
                    System.out.println(CLIColors.ANSI_GREEN + ((ReqPlayersMessage) evt.getNewValue()).getMessage() + CLIColors.RESET);
            case ServerMessageHandler.CUSTOM_MESSAGE_LISTER ->
                    System.out.println(((CustomMessage) evt.getNewValue()).getMessage());
            case ServerMessageHandler.GAME_STATE_LISTENER ->
                    statePrinter((GameState) evt.getOldValue(), (GameState) evt.getNewValue());
            case ServerMessageHandler.NEXT_ROUNDOWNER_LISTENER ->
                    roundPrinter((String) evt.getOldValue(), (String) evt.getNewValue());
            case ServerMessageHandler.PLAYED_CARD_LISTENER -> {
                    PlayedCardMessage msg = (PlayedCardMessage) evt.getNewValue();
                    System.out.println(CLIColors.ANSI_GREEN+ "\t" + msg.getPlayer() + " played: " + msg.getMessage() + CLIColors.RESET);
            }
            // TODO case activate card ...
        }

    }
    /**
     * Print the state of the game during the game itself
     */
    public void statePrinter(GameState oldState, GameState newState) {

        String owner = modelView.getRoundOwner();
        String player = modelView.getPlayerName();

        if (player.equals(owner)) {
//            if(oldState != newState){
            System.out.println(CLIColors.ANSI_GREEN + "You are in " + newState + " state, make your choice" + CLIColors.RESET); // TODO differenziare la frase tramite funzione a seconda dello stato custom
//            }
        } else {
//            if(oldState != newState){
            System.out.println(CLIColors.ANSI_CYAN + "Player " + owner + " is in " + newState + CLIColors.RESET); // TODO differenziare la frase tramite funzione a seconda dello stato custom
//            }
        }
    }
    /**
     * Print the owner of the turn  during the game
     */
    public void roundPrinter(String oldRoundOwner, String newRoundOwner) {
        String player = modelView.getPlayerName();
        if (!newRoundOwner.equals(oldRoundOwner)) {
            //clearScreen();  // FIXME Seams not to work
            if (player.equals(newRoundOwner)) {
                System.out.println(CLIColors.ANSI_BACKGROUND_BLACK.getEscape() + CLIColors.ANSI_WHITE + "You are the new round owner" + CLIColors.RESET);
            } else {
                System.out.println(CLIColors.ANSI_CYAN + newRoundOwner + "is the new round owner" + CLIColors.RESET);
            }
        }
    }

    /**
     * IsActives give us if the game is in exec
     */
    public boolean isActiveGame() {
        return activeGame;
    }
    /**
     * Clean the CLI of the users
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    /**
     * Show possible actions or model representation
     * @return true if cmd is a model-show command, false otherwise
     */
    private boolean handleView(String cmd) {
        String[] in = cmd.split(" ");
        boolean isModelShowCommand = true;
        if (in.length > 0) {
            String command = in[0];

            switch (command.toUpperCase()) {
                case "SCHOOL" -> {
                    if(in.length > 1) {
                        String nickname = in[1];
                        showSchool(modelView, nickname);
                    } else {
                        showSchool(modelView);
                    }
                }
                case "ACTIONS", "HELP" -> showCLICommands();
                case "BOARD" -> showBoard();
                case "CLOUDS" -> showClouds();
                case "CARDS" -> showCards();
                case "PROFS" -> showProfs();
                default -> isModelShowCommand = false;
            }
            if(modelView.getExpert()){
                switch (command.toUpperCase()){
                    case "BALANCE" -> {showBalance(); isModelShowCommand = true;}
                    case "CHARACTERCARDS" -> {showCharacters(); isModelShowCommand = true;}
                }
            }
        }else {
            isModelShowCommand = false;
        }
        return isModelShowCommand;
    }


    /**
     * The method showSchool prints the content of the player's school on the CLI
     * @param modelView
     */
    private void showSchool(ModelView modelView) {
        Printable.printSchool(modelView.getPlayerMapSchool().get(modelView.getPlayerName()));
    }

    /**
     * The method showSchool prints the content of the specified player's school on the CLI
     * @param modelView
     * @param nickname
     */
    private void showSchool(ModelView modelView, String nickname) {
        Map<String, School> schools = modelView.getPlayerMapSchool();
        if (schools.containsKey(nickname)) {
            School school = schools.get(nickname);
            Printable.printSchool(school);
        }
    }

    /**
     * Possible actions based on state game and expert mode
     */
    private void showCLICommands() {
        // MODEL VIEW AVAILABLE COMMANDS
        System.out.println(CLIColors.ANSI_GREEN + "These are all the possible show command available: " + CLIColors.RESET);
        System.out.println(CLIColors.ANSI_BLUE + "\t" +(modelView.getExpert() ? SHOW_COMMANDS_EXPERT : SHOW_COMMANDS) +
                " [?user] (ex: show school user)" + CLIColors.RESET);

        // ACTIONS AVAILABLE COMMANDS
        if(Objects.equals(modelView.getRoundOwner(), modelView.getPlayerName())) {  // actions are available only if it's my turn
            // modelView.getPlayedCards().getOrDefault(modelView.getRoundOwner()); bho poi spiego
            System.out.println(CLIColors.ANSI_GREEN + "These are all the possible actions available at this moment: " + CLIColors.RESET);
            List<Integer> availableClouds = IntStream.range(0, modelView.getClouds().size())
                    .filter(i -> !modelView.getClouds().get(i)
                            .isEmpty()).boxed().toList();

            switch (modelView.getGameState()) {
                case SETUP_CHOOSE_MAGICIAN -> System.out.println(CLIColors.ANSI_BLUE + "\t magician " + modelView.getAvailableMagiciansStr() + CLIColors.RESET); // CHECK i mean available magicians
                case PLANNING_CHOOSE_CARD -> System.out.println(CLIColors.ANSI_BLUE + "\t playcard " + modelView.getHand().stream().map(AssistantCard::getValue).toList() + CLIColors.RESET);
                case ACTION_MOVE_STUDENTS ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t studentisland \n studentshall" + CLIColors.RESET);
                case ACTION_MOVE_MOTHER ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t movemother 1 - " + modelView.getPlayedCards().get(modelView.getPlayerName()).getMaxMoves() + CLIColors.RESET);
                case ACTION_CHOOSE_CLOUD -> System.out.println(CLIColors.ANSI_BLUE + "\t cloud " + availableClouds + CLIColors.RESET);
            }
            if (modelView.getExpert()) {
                if (modelView.getGameState() != GameState.SETUP_CHOOSE_MAGICIAN) {
                    String characters = modelView.getCharacterCards().stream().map(c -> c.name).toList().toString(); // todo in teoria dal codice del activate si può attivare solo in movestudent e altro stato e basta
                    String activeCharacters = modelView.getCharacterCards().stream().filter(c->c.isActive).map(c -> c.name).toList().toString(); // todo in teoria dal codice del activate si può attivare solo in movestudent e altro stato e basta
                    System.out.println(CLIColors.ANSI_BLUE + "\t activate " + characters + CLIColors.RESET);
                    System.out.println(CLIColors.ANSI_BLUE + "\t deactivate " + activeCharacters + CLIColors.RESET);
                }
            }
        }
    }

    private void showBalance(){
        System.out.println(CLIColors.ANSI_BLUE + "Your Balance:  " + modelView.getBalance() + CLIColors.RESET);
    }

    private void showCards(){
        System.out.println(CLIColors.ANSI_BLUE + "Your Card:  " + CLIColors.RESET);
        modelView.getHand().stream().map(c -> CLIColors.ANSI_BLUE + "\t" + c
                + CLIColors.RESET).forEach(System.out::println);
    }
    private void showCharacters(){
        modelView.getCharacterCards().stream().map(c -> CLIColors.ANSI_BLUE + "\t" + c.name + ", price: " + c.price +
                ", state: " + (c.isActive?"active": "no-active") + CLIColors.RESET).forEach(System.out::println);
    }

    private void showProfs(){
        modelView.getProfessors().entrySet().stream()
                .map(p -> CLIColors.ANSI_BLUE + "\t" +p.getKey() + "-> " + (p.getValue() == null ? "_" : p.getValue()) + CLIColors.RESET)
                .forEach(System.out::println);
    }


    /**
     * Board print
     */
    private void showBoard() {
        Printable.printBoard(modelView.getIslandContainer(), modelView.getMotherNature(), modelView.getPlayers(), modelView.getPlayerMapSchool());
    }
    /**
     * Clouds print
     */
    private void showClouds() {
        Printable.printClouds(modelView.getClouds());
    }
    /**
     * input validation method: true if the string has matched with the pattern of Regex
     */
    private boolean validateIp(String ip){

        boolean validate = false;
        ip = ip.toLowerCase();
        Pattern p = Pattern.compile("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b"); //pattern for validate the ip address
        Matcher m = p.matcher(ip);
        if(m.matches() || ip.equals("localhost")){
            validate = true;
        }
        return validate;

    }
    /**
     * input validation method: true if the string has matched with the pattern of Regex
     * @return integer because we passed a string (the pattern of regex required a string) and after we cast the port
     */
    private int validatePort(String Port){

        Pattern p = Pattern.compile("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$"); //pattern tto validate port
        Matcher m = p.matcher(Port);
        int port = Integer.parseInt(Port);

         if( !(port > 1023 && m.matches())) { // below 1023 the are ports reserved for OS
             return -1;
         }
        return port;
    }
    /**
     * input validation method: true if the string has matched with the pattern of Regex
     */
    private boolean validateNickname(String user ){
        boolean validate = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){2,18}[a-zA-Z0-9]$");
        Matcher m = p.matcher(user);
        if(m.matches()){
            validate = true;
        }
        return validate;
    }


}