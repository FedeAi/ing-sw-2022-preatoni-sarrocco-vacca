package it.polimi.ingsw.Client.cli;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Constants.*;
import it.polimi.ingsw.Constants.Character;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import it.polimi.ingsw.Server.Answer.GameError;
import it.polimi.ingsw.Server.Answer.ReqPlayersMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
    private final String SHOW_ERROR = "SHOW not well formatted: show [SCHOOL/ACTIONS/BOARD/CLOUDS] [?user] (ex: show school user)";

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
//        System.out.println(">Insert the server IP address");
//        System.out.print(">");
//        String ip = scanner.nextLine();
//        validateIp(ip);
//        System.out.println(">Insert the server port");
//        System.out.print(">");
//        String port = scanner.nextLine();
//        validatePort(port);
//
//       }while(validatePort(port) == -1 || validateIp(ip) = false);
        Constants.setAddress("localhost");
        Constants.setPort(8080);
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

            switch (cmd.split(" ")[0].toUpperCase()) { //splitting the command due there are two possible macro moves: show (printable command) and the effective actions
                case "SHOW" -> handleView(cmd);  //printable command
                default -> listeners.firePropertyChange("action", null, cmd); //if isn't present 'show', then firepropertychange
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
            System.out.println(CLIColors.ANSI_CYAN + "Player " + player + "is in " + newState + CLIColors.RESET); // TODO differenziare la frase tramite funzione a seconda dello stato custom
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
     * Hold the possible items that we should print
     */
    private void handleView(String cmd) {
        String[] in = cmd.split(" ");
        if (in.length > 1) { //if the show has enough arc
            String command = in[1];

            switch (command.toUpperCase()) {
                case "SCHOOL" -> {
                    if(in.length > 2) {
                        String nickname = in[2];
                        showSchool(modelView, nickname);
                    }}
                case "ACTIONS" -> showActions();
                case "BOARD" -> showBoard();
                case "CLOUDS" -> showClouds();
                default -> System.out.println(CLIColors.ANSI_RED + SHOW_ERROR + CLIColors.RESET);
            }
        }
    }
    /**
     * School print
     */
    private void showSchool(ModelView modelView, String key) {
     /*   Map<String, School> schoolMap = modelView.getPlayerMapSchool();
        if(key == null){ //then we print the school for each player
            for(int i = 0; i < schoolMap.keySet().size(); i++); //iterate each player

        }
        else if(key != null && schoolMap.containsKey(key) ){
            modelView.getPlayerMapSchool().keySet().contains()
        }
   */

    }

    /**
     * Possible actions based on state game and expert mode
     */
    private void showActions() {

            List<Integer> availableClouds = IntStream.range(0, modelView.getClouds().size())
                    .filter(i -> !modelView.getClouds().get(i)
                            .isEmpty()).boxed().toList();


        // modelView.getPlayedCards().getOrDefault(modelView.getRoundOwner()); bho poi spiego
        System.out.println(CLIColors.ANSI_GREEN + "These are all the possible moves available at this moment: " + CLIColors.RESET);

        switch (modelView.getGameState()) {
            case SETUP_CHOOSE_MAGICIAN -> System.out.println(CLIColors.ANSI_GREEN + "magician " + modelView.getAvailableMagiciansStr() + CLIColors.RESET); // CHECK i mean available magicians
            case PLANNING_CHOOSE_CARD -> System.out.println(CLIColors.ANSI_GREEN + "playcard " + modelView.getHand().stream().map(AssistantCard::getValue).toList() + CLIColors.RESET);
            case ACTION_MOVE_STUDENTS -> System.out.println(CLIColors.ANSI_GREEN + " studentisland \n studentshall"  + CLIColors.RESET);
            case ACTION_MOVE_MOTHER -> System.out.println(CLIColors.ANSI_GREEN + " movemother 0 - "+modelView.getPlayedCards().get(modelView.getPlayerName()).getMaxMoves()  + CLIColors.RESET);
            case ACTION_CHOOSE_CLOUD -> System.out.println(CLIColors.ANSI_GREEN + " cloud " + availableClouds + CLIColors.RESET);
        }
        if(modelView.getExpert()){
            if(modelView.getGameState() != GameState.SETUP_CHOOSE_MAGICIAN){
                System.out.println("Characters:");
                modelView.getCharacterCards().stream().map(c -> c.getCharacter().toString()).forEach(System.out::println);
                System.out.println(CLIColors.ANSI_BLUE + "Your Balance:  " + modelView.getBalance() + CLIColors.RESET);
            }
        }

    }
    /**
     * Board print
     */
    private void showBoard() {
        Printable.printBoard(modelView.getIslandContainer(), modelView.getClouds(), modelView.getMotherNature(), modelView.getPlayers(), modelView.getPlayerMapSchool());
    }
    /**
     * Clouds print
     */
    private void showClouds() {
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