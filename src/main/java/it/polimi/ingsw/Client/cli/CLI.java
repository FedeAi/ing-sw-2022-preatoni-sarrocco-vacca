package it.polimi.ingsw.Client.cli;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Constants.CLIColors;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Constants.Printable;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import it.polimi.ingsw.Server.Answer.GameError;
import it.polimi.ingsw.Server.Answer.ReqPlayersMessage;
import it.polimi.ingsw.Server.Answer.WinMessage;
import it.polimi.ingsw.Server.Answer.modelUpdate.PlayedCardMessage;

import java.awt.desktop.ScreenSleepEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static it.polimi.ingsw.Constants.Constants.validateNickname;

/**
 * CLI is the main class for everything regarding the CLI client.
 * It parses the user input and prints all that's needed to play the game.
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


        String ip;
        String port;

        do {
            clearScreen(); //cleaning terminale
            System.out.println(Constants.ERIANTYS);
            System.out.println(Constants.AUTHORS);
            Scanner scanner = new Scanner(System.in);
            System.out.println(">Insert the server IP address");
            System.out.print(">");
            ip = scanner.nextLine();

            System.out.println(">Insert the server port");
            System.out.print(">");
            port = scanner.nextLine();

        } while (Constants.validatePort(port) == -1 || Constants.validateIp(ip));

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
            while ((cmd = input.nextLine()).isEmpty()) {
            } //bug fixed for Linux: scanner take also 'enter' like a command

            // cli commands can be model-show or action commands, if it's not a model-show command it's an action command
            if (!handleView(cmd)) {
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
            } while (!validateNickname(nickname));

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
            System.out.println(CLIColors.ANSI_GREEN + "Socket Connection setup completed!" + CLIColors.RESET);
        } catch (DuplicateNicknameException | InvalidNicknameException e) {
            setup();
        }
        listeners.addPropertyChangeListener("action", new InputToMessage(modelView, connectionSocket));
    }

    /**
     * Listener pattern implementation.
     * This prints all the necessary information from the server at the moment of reception.
     * @param evt the information to be printed
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
                System.out.println(CLIColors.ANSI_GREEN + "\t" + msg.getPlayer() + " played: " + msg.getMessage() + CLIColors.RESET);
            }

            case ServerMessageHandler.WIN_MESSAGE_LISTER -> {
                WinMessage msg = (WinMessage) evt.getNewValue();
                printWinner(msg.getMessage());
            }

            // TODO case activate card ...
        }

    }

    private void printWinner(String player) {
        if(player.equals(modelView.getPlayerName())){
            System.out.println(CLIColors.ANSI_GREEN + "\n\n\n\t" + "#############################################"
                    + "\n\n\n\t" + "YOU WON   :))))" +
                    "\n\n\n\t" + "#############################################"
                            +CLIColors.RESET);
        }else{
            System.out.println(CLIColors.ANSI_RED + "\n\n\n\t" + "#############################################"
                    + "\n\n\n\t" + player + "won :(" +
                    "\n\n\n\t" + "#############################################"
                    +CLIColors.RESET);
        }


    }

    /**
     * Prints the current state and the current player of the game.
     * @param oldState the old state of the game
     * @param newState the new state of the game
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
     * Prints the owner of the turn during the game
     * @param oldRoundOwner the previous round owner
     * @param newRoundOwner the current round owner
     */
    public void roundPrinter(String oldRoundOwner, String newRoundOwner) {
        String player = modelView.getPlayerName();
        if (!newRoundOwner.equals(oldRoundOwner)) {
            //clearScreen();  // FIXME Seams not to work
            if (player.equals(newRoundOwner)) {
                System.out.println(CLIColors.ANSI_BACKGROUND_BLACK.getEscape() + CLIColors.ANSI_WHITE + "You are the new round owner" + CLIColors.RESET);
            } else {
                System.out.println(CLIColors.ANSI_CYAN + newRoundOwner + " is the new round owner" + CLIColors.RESET);
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
     * Clean the CLI of the user
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
     * Shows the possible actions or the model representation
     * @return true if the input is correct, false otherwise
     */
    private boolean handleView(String cmd) {
        String[] in = cmd.split(" ");
        boolean isModelShowCommand = true;
        if (in.length > 0) {
            String command = in[0];

            switch (command.toUpperCase()) {
                case "SCHOOL" -> {
                    if (in.length > 1) {
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
            if (modelView.getExpert()) {
                switch (command.toUpperCase()) {
                    case "BALANCE" -> {
                        showBalance();
                        isModelShowCommand = true;
                    }
                    case "CHARACTERCARDS" -> {
                        showCharacters();
                        isModelShowCommand = true;
                    }
                }
            }
        } else {
            isModelShowCommand = false;
        }
        return isModelShowCommand;
    }


    /**
     * The method showSchool prints the content of the player's school on the CLI
     * @param modelView the client's model representation
     */
    private void showSchool(ModelView modelView) {
        Printable.printSchool(modelView.getPlayerMapSchool().get(modelView.getPlayerName()));
    }

    /**
     * The method showSchool prints the content of the specified player's school on the CLI
     * @param modelView the client's model representation
     * @param nickname the specified player's nickname
     */
    private void showSchool(ModelView modelView, String nickname) {
        Map<String, School> schools = modelView.getPlayerMapSchool();
        if (schools.containsKey(nickname)) {
            School school = schools.get(nickname);
            Printable.printSchool(school);
        }
    }

    /**
     *  Possible actions based on state game and expert mode.
     */
    private void showCLICommands() {
        // MODEL VIEW AVAILABLE COMMANDS
        System.out.println(CLIColors.ANSI_GREEN + "These are all the possible show command available: " + CLIColors.RESET);
        System.out.println(CLIColors.ANSI_BLUE + "\t" + (modelView.getExpert() ? SHOW_COMMANDS_EXPERT : SHOW_COMMANDS) +
                " [?user] (ex: show school user)" + CLIColors.RESET);

        // ACTIONS AVAILABLE COMMANDS
        if (Objects.equals(modelView.getRoundOwner(), modelView.getPlayerName())) {  // actions are available only if it's my turn
            // modelView.getPlayedCards().getOrDefault(modelView.getRoundOwner()); bho poi spiego
            System.out.println(CLIColors.ANSI_GREEN + "These are all the possible actions available at this moment: " + CLIColors.RESET);
            List<Integer> availableClouds = IntStream.range(0, modelView.getClouds().size())
                    .filter(i -> !modelView.getClouds().get(i)
                            .isEmpty()).boxed().toList();

            switch (modelView.getGameState()) {
                case SETUP_CHOOSE_MAGICIAN ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t magician " + modelView.getAvailableMagiciansStr() + CLIColors.RESET); // CHECK i mean available magicians
                case PLANNING_CHOOSE_CARD ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t playcard " + modelView.getHand().stream().map(AssistantCard::getValue).toList() + CLIColors.RESET);
                case ACTION_MOVE_STUDENTS ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t studentisland \n studentshall" + CLIColors.RESET);
                case ACTION_MOVE_MOTHER ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t movemother 1 - " + modelView.getPlayedCards().get(modelView.getPlayerName()).getMaxMoves() + CLIColors.RESET);
                case ACTION_CHOOSE_CLOUD -> System.out.println(CLIColors.ANSI_BLUE + "\t cloud " + availableClouds + CLIColors.RESET);
            }
            if (modelView.getExpert()) {
                if (modelView.getGameState() != GameState.SETUP_CHOOSE_MAGICIAN) {
                    String characters = modelView.getCharacterCards().stream().map(c -> c.name).toList().toString(); // todo in teoria dal codice del activate si può attivare solo in movestudent e altro stato e basta
                    String activeCharacters = modelView.getCharacterCards().stream().filter(c -> c.isActive).map(c -> c.name).toList().toString(); // todo in teoria dal codice del activate si può attivare solo in movestudent e altro stato e basta
                    System.out.println(CLIColors.ANSI_BLUE + "\t activate " + characters + CLIColors.RESET);
                    System.out.println(CLIColors.ANSI_BLUE + "\t deactivate " + activeCharacters + CLIColors.RESET);
                }
            }
        }
    }

    /**
     *  Prints the player's balance.
     */
    private void showBalance(){
        System.out.println(CLIColors.ANSI_BLUE + "Your Balance:  " + modelView.getBalance() + CLIColors.RESET);
    }

    /**
     *  Prints the player's hand.
     */
    private void showCards(){
        System.out.println(CLIColors.ANSI_BLUE + "Your Cards:  " + CLIColors.RESET);
        modelView.getHand().stream().map(c -> CLIColors.ANSI_BLUE + "\t" + c
                + CLIColors.RESET).forEach(System.out::println);
    }

    /**
     *  This method returns the character cards present in the current game.
     */
    private void showCharacters(){
        modelView.getCharacterCards().stream().map(c -> CLIColors.ANSI_BLUE + "\t" + c.name + ", price: " + c.price +
                ", state: " + (c.isActive ? "active" : "no-active") + CLIColors.RESET).forEach(System.out::println);
    }

    /**
     *  Prints the current owners of the game's professors.
     */
    private void showProfs(){
        modelView.getProfessors().entrySet().stream()
                .map(p -> CLIColors.ANSI_BLUE + "\t" + p.getKey() + "-> " + (p.getValue() == null ? "_" : p.getValue()) + CLIColors.RESET)
                .forEach(System.out::println);
    }


    /**
     *  Prints the representation of the game's board (the islands).
     */
    private void showBoard() {
        Printable.printBoard(modelView.getIslandContainer(), modelView.getMotherNature(), modelView.getPlayers(), modelView.getPlayerMapSchool());
    }

    /**
     *  Prints the representation of the game's clouds.
     */
    private void showClouds() {
        Printable.printClouds(modelView.getClouds());
    }


}