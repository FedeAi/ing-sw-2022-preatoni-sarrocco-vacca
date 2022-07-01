package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.constants.CLIColors;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.exceptions.InvalidNicknameException;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.constants.Printable;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.School;
import it.polimi.ingsw.model.islands.IslandContainer;
import it.polimi.ingsw.server.answers.CustomMessage;
import it.polimi.ingsw.server.answers.GameError;
import it.polimi.ingsw.server.answers.ReqPlayersMessage;
import it.polimi.ingsw.server.answers.WinMessage;
import it.polimi.ingsw.server.answers.model.PlayedCardMessage;

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
import java.util.stream.IntStream;

import static it.polimi.ingsw.constants.Constants.validateNickname;

/**
 * CLI is the main class for everything regarding the CLI client.
 * It parses the user input and prints all that's needed to play the game.
 *
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
     * Constructor CLI creates a new CLI instance.
     */
    public CLI() {
        activeGame = true;
        input = new Scanner(System.in);
        modelView = new ModelView(this);
        output = new PrintStream(System.out);
        serverMessageHandler = new ServerMessageHandler(this, modelView);
    }

    /**
     * The CLI main method. It creates the CLI instance and lets the user select the Server's IP and port.
     *
     * @param args the program arguments.
     */
    public static void main(String[] args) {
        String ip;
        String port;
        do {
            // Clears the current terminal screen
            clearScreen();
            System.out.println(Constants.ERIANTYS);
            System.out.println(Constants.AUTHORS);
            Scanner scanner = new Scanner(System.in);
            // Input the Server IP
            System.out.println(">Insert the server IP address");
            System.out.print(">");
            ip = scanner.nextLine();
            // Input the Server port
            System.out.println(">Insert the server port");
            System.out.print(">");
            port = scanner.nextLine();
        } while (Constants.validatePort(port) == -1 || !Constants.validateIP(ip));
        Constants.setAddress(ip);
        Constants.setPort(Integer.parseInt(port));
        CLI cli = new CLI();
        cli.run();
    }

    /**
     * Method run listens for the CLI input until the game closes.
     */
    public void run() {
        setup();
        while (isActiveGame()) {
            input.reset();
            String cmd;
            while ((cmd = input.nextLine()).isEmpty()) {
            } // Bug fixed for Linux: scanner take also 'enter' like a command
            // Cli commands can be show or action commands
            if (!handleView(cmd)) {
                // If it's not a show command it's an action command
                listeners.firePropertyChange("action", null, cmd);
            }
        }
        input.close();
        output.close();
    }

    /**
     * method setup asks the username and establishes the connection with the server.
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
            System.out.println(">You chose: " + nickname);
            System.out.println(">Is it ok? [y/n] ");
            System.out.print(">");
            if (input.nextLine().equalsIgnoreCase("y")) {
                confirmation = true;
            } else {
                nickname = null;
            }
        }
        connectionSocket = new ConnectionSocket();
        modelView.setPlayerName(nickname);
        try {
            if (!connectionSocket.setup(nickname, modelView, serverMessageHandler)) {
                System.err.println("The entered IP/port doesn't match any active server or the server is not " + "running. Please try again!");
                CLI.main(null);
            }
        } catch (DuplicateNicknameException | InvalidNicknameException e) {
            setup();
        }
        listeners.addPropertyChangeListener("action", new InputToMessage(modelView, connectionSocket));
    }

    /**
     * Listener pattern implementation.
     * This prints all the necessary information from the server at the moment of reception.
     *
     * @param evt the information to be printed
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        listeners.firePropertyChange(evt);
        switch (evt.getPropertyName()) {
            case ServerMessageHandler.GAME_ERROR_LISTENER ->
                    System.out.println(CLIColors.ANSI_RED + ((GameError) evt.getNewValue()).getMessage() + CLIColors.RESET);
            case ServerMessageHandler.REQ_PLAYERS_LISTENER ->
                    System.out.println(CLIColors.ANSI_GREEN + ((ReqPlayersMessage) evt.getNewValue()).getMessage() + CLIColors.RESET);
            case ServerMessageHandler.CUSTOM_MESSAGE_LISTENER ->
                    System.out.println(((CustomMessage) evt.getNewValue()).getMessage());
            case ServerMessageHandler.GAME_STATE_LISTENER ->
                    statePrinter((GameState) evt.getOldValue(), (GameState) evt.getNewValue());
            case ServerMessageHandler.NEXT_ROUNDOWNER_LISTENER ->
                    roundPrinter((String) evt.getOldValue(), (String) evt.getNewValue());
            case ServerMessageHandler.PLAYED_CARD_LISTENER -> {
                PlayedCardMessage msg = (PlayedCardMessage) evt.getNewValue();
                System.out.println(CLIColors.ANSI_GREEN + "\t" + msg.getPlayer() + " played: " + msg.getMessage() + CLIColors.RESET);
            }
            case ServerMessageHandler.WIN_MESSAGE_LISTENER -> {
                WinMessage msg = (WinMessage) evt.getNewValue();
                printWinner(msg.getMessage());
            }
            // TODO case activate card ...
        }
    }

    /**
     * Method printWinner prints the current game's winner.
     *
     * @param player the winner's nickname.
     */
    private void printWinner(String player) {
        if (player.equals(modelView.getPlayerName())) {
            System.out.println(CLIColors.ANSI_GREEN + "\n\n\n\t" + "#############################################"
                    + "\n\n\n\t" + "YOU WON   :))))" +
                    "\n\n\n\t" + "#############################################"
                    + CLIColors.RESET);
        } else {
            System.out.println(CLIColors.ANSI_RED + "\n\n\n\t" + "#############################################"
                    + "\n\n\n\t" + player + "won :(" +
                    "\n\n\n\t" + "#############################################"
                    + CLIColors.RESET);
        }
    }

    /**
     * Method statePrinter prints the current game state and the current round owner.
     *
     * @param oldState the old state of the game.
     * @param newState the new state of the game.
     */
    public void statePrinter(GameState oldState, GameState newState) {
        String owner = modelView.getRoundOwner();
        String player = modelView.getPlayerName();
        if (player.equals(owner)) {
            if (oldState != newState) {
                System.out.println(CLIColors.ANSI_GREEN + "You are in " + newState + " state, please make your choice" + CLIColors.RESET);
            } else {
                System.out.println(CLIColors.ANSI_GREEN + "It is still your turn, check the possibile moves with the 'help' command" + CLIColors.RESET);
            }
        } else {
            if (oldState != newState) {
                System.out.println(CLIColors.ANSI_CYAN + "Player " + owner + " is in " + newState + CLIColors.RESET);
            } else {
                System.out.println(CLIColors.ANSI_CYAN + "Player " + owner + " is still the owner, wait for your turn" + CLIColors.RESET);
            }
        }
    }

    /**
     * Method roundPrinter prints on the screen the round owner updates.
     *
     * @param oldRoundOwner the previous round owner.
     * @param newRoundOwner the current round owner.
     */
    public void roundPrinter(String oldRoundOwner, String newRoundOwner) {
        String player = modelView.getPlayerName();
        if (!newRoundOwner.equals(oldRoundOwner)) {
            clearScreen();
            if (player.equals(newRoundOwner)) {
                System.out.println(CLIColors.ANSI_PINK.getEscape() + CLIColors.ANSI_WHITE + "You are the new round owner" + CLIColors.RESET);
            } else {
                System.out.println(CLIColors.ANSI_CYAN + newRoundOwner + " is the new round owner" + CLIColors.RESET);
            }
        }
    }

    /**
     * Method isActiveGame returns if the game is active.
     *
     * @return true if the game is active, false otherwise
     */
    public boolean isActiveGame() {
        return activeGame;
    }

    /**
     * Method clearScreen clears the user's terminal.
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
     * Method handleView shows the possible actions or the graphical board.
     *
     * @return True if the input is correct, false otherwise.
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
     * The method showSchool prints the content of the player's school on the CLI.
     *
     * @param modelView the client's model representation.
     * @see Printable#printSchool(School)
     */
    private void showSchool(ModelView modelView) {
        Printable.printSchool(modelView.getPlayerMapSchool().get(modelView.getPlayerName()));
    }

    /**
     * The method showSchool prints the content of the specified player's school on the CLI.
     *
     * @param modelView the client's model representation.
     * @param nickname  the specified player's nickname.
     * @see Printable#printSchool(School)
     */
    private void showSchool(ModelView modelView, String nickname) {
        Map<String, School> schools = modelView.getPlayerMapSchool();
        if (schools.containsKey(nickname)) {
            School school = schools.get(nickname);
            Printable.printSchool(school);
        }
    }

    /**
     * Method showCLICommands shows the available actions based on state game and game mode.
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
                case ACTION_CHOOSE_CLOUD ->
                        System.out.println(CLIColors.ANSI_BLUE + "\t cloud " + availableClouds + CLIColors.RESET);
            }
            if (modelView.getExpert()) {
                if (modelView.getGameState() != GameState.SETUP_CHOOSE_MAGICIAN) {
                    String characters = modelView.getCharacterCards().stream().map(c -> c.type).toList().toString();
                    String activeCharacters = modelView.getCharacterCards().stream().filter(c -> c.isActive).map(c -> c.type).toList().toString();
                    System.out.println(CLIColors.ANSI_BLUE + "\t activate " + characters + CLIColors.RESET);
                    System.out.println(CLIColors.ANSI_BLUE + "\t deactivate " + activeCharacters + CLIColors.RESET);
                }
            }
        }
    }

    /**
     * Method showBalance prints the player's current balance.
     */
    private void showBalance() {
        System.out.println(CLIColors.ANSI_BLUE + "Your Balance:  " + modelView.getBalance() + CLIColors.RESET);
    }

    /**
     * Method showCards prints the player's current hand.
     */
    private void showCards() {
        System.out.println(CLIColors.ANSI_BLUE + "Your Cards:  " + CLIColors.RESET);
        modelView.getHand().stream().map(c -> CLIColors.ANSI_BLUE + "\t" + c
                + CLIColors.RESET).forEach(System.out::println);
    }

    /**
     * Method showCharacters prints the character cards present in the current game.
     */
    private void showCharacters() {
        modelView.getCharacterCards().stream().map(c -> CLIColors.ANSI_BLUE + "\t" + c.type + ", price: " + c.price +
                ", state: " + (c.isActive ? "active" : "no-active") + CLIColors.RESET).forEach(System.out::println);
    }

    /**
     * Method showProfs prints the current owners of the game's professors.
     */
    private void showProfs() {
        modelView.getProfessors().entrySet().stream()
                .map(p -> CLIColors.ANSI_BLUE + "\t" + p.getKey() + "-> " + (p.getValue() == null ? "_" : p.getValue()) + CLIColors.RESET)
                .forEach(System.out::println);
    }

    /**
     * Method showBoard prints the representation of the game's board (the islands).
     *
     * @see Printable#printBoard(IslandContainer, int, List, Map)
     */
    private void showBoard() {
        Printable.printBoard(modelView.getIslandContainer(), modelView.getMotherNature(), modelView.getPlayers(), modelView.getPlayerMapSchool());
    }

    /**
     * Method showClouds prints the representation of the game's clouds.
     *
     * @see Printable#printClouds(List)
     */
    private void showClouds() {
        Printable.printClouds(modelView.getClouds());
    }
}