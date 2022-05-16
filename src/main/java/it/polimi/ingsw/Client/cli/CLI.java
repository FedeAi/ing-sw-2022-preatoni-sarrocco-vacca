package it.polimi.ingsw.Client.cli;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import it.polimi.ingsw.Server.Answer.GameError;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLI implements UI {

    private static final Logger logger = Logger.getLogger(CLI.class.getName());
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private final PrintStream output;
    private final Scanner input;
    private final ModelView modelView;
    private final ServerMessageHandler serverMessageHandler;
    private boolean activeGame;
    private final String SHOW_ERROR = "syntax show error: show #item (ex: show school) ";

    private ConnectionSocket connectionSocket;

    public CLI() {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        modelView = new ModelView(this);
        serverMessageHandler = new ServerMessageHandler(this, modelView);
        activeGame = true;

    }

    public static void main(String[] args) {

        System.out.println(Constants.ERIANTYS);
        System.out.println(Constants.AUTHORS);
        Scanner scanner = new Scanner(System.in);

//        System.out.println(">Insert the server IP address");
//        System.out.print(">");
//        String ip = scanner.nextLine();
//
//        System.out.println(">Insert the server port");
//        System.out.print(">");
//        int port = scanner.nextInt();
        Constants.setAddress("localhost");
        Constants.setPort(8080);
        CLI cli = new CLI();
        cli.run();
    }

    public void run() {
        setup();
        while (isActiveGame()) {
            input.reset();
//            String cmd = input.nextLine();
            String cmd;
            while ((cmd = input.nextLine()).isEmpty()) {}   // While loop needed to handle the case the input string is "", ( linux problem )

            switch (cmd.split(" ")[0].toUpperCase()){
                case "SHOW" -> handleView(cmd);  //printable command
                default -> listeners.firePropertyChange("action", null, cmd);
            }


        }
        input.close();
        output.close();
    }

    public void setup() {

        String nickname = null;
        boolean confirmation = false;

        while (!confirmation) {
            do {
                System.out.println(">Insert your nickname: ");
                System.out.print(">");
                nickname = input.nextLine();
            } while (nickname == null);

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
            System.out.println(/*nameMapColor.get(GREEN)+ */  "Socket Connection setup completed!" /* + nameMapColor.get("RST")*/);
        } catch (DuplicateNicknameException | InvalidNicknameException e) {
            setup();
        }
        listeners.addPropertyChangeListener("action", new InputToMessage(modelView, connectionSocket));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ServerMessageHandler.GAME_ERROR_LISTENER ->  System.out.println(((GameError) evt.getNewValue()).getMessage());
            case ServerMessageHandler.CUSTOM_MESSAGE_LISTER -> System.out.println(((CustomMessage) evt.getNewValue()).getMessage());
            case ServerMessageHandler.GAME_STATE_LISTENER -> statePrinter((GameState) evt.getOldValue(), (GameState) evt.getNewValue());
            case ServerMessageHandler.NEXT_ROUNDOWNER_LISTENER -> roundPrinter((String) evt.getOldValue(), (String) evt.getNewValue());
        }

    }

    public void statePrinter(GameState oldState, GameState newState){

        String owner = modelView.getRoundOwner();
        String player = modelView.getPlayerName();

        if(player.equals(owner)) {

//            if(oldState != newState){
                System.out.println(Constants.ANSI_GREEN +"You are in " + newState + " state, make your choice" + Constants.ANSI_RESET); // TODO differenziare la frase tramite funzione a seconda dello stato custom
//            }
        }else{
//            if(oldState != newState){
                System.out.println(Constants.ANSI_CYAN + "Player " + player + "is in " + newState + Constants.ANSI_RESET); // TODO differenziare la frase tramite funzione a seconda dello stato custom
//            }
        }
    }

    public void roundPrinter(String oldRoundOwner, String newRoundOwner){
        String player = modelView.getPlayerName();
        if(!newRoundOwner.equals(oldRoundOwner)) {
            if (player.equals(newRoundOwner)) {
                System.out.println(Constants.ANSI_BACKGROUND_BLACK + Constants.ANSI_WHITE + "You are the new round owner"  + Constants.ANSI_RESET);
            }else{
                System.out.println(Constants.ANSI_CYAN + newRoundOwner + "is the new round owner" + Constants.ANSI_RESET);
            }
        }
    }


    public boolean isActiveGame() {
        return activeGame;
    }
    public static void clearScreen() {
        try{
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    private void handleView(String cmd){
        String[] in = cmd.split(" ");
        if(in.length > 1) {
            String command = in[1];

            switch (command.toUpperCase()) {
                case "SCHOOL" -> showSchool(cmd);
                case "ACTIONS" ->showActions() ;
                case "BOARD" -> showBoard() ;
                case "CLOUDS" -> showClouds();
                default ->  System.out.println(Constants.ANSI_RED + SHOW_ERROR + Constants.ANSI_RESET);
            }
        }
    }
}
