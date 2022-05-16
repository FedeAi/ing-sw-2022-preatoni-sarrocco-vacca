package it.polimi.ingsw.Client.cli;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import it.polimi.ingsw.Server.Answer.GameError;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;
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

        System.out.println(">Insert the server IP address");
        System.out.print(">");
        String ip = scanner.nextLine();

        System.out.println(">Insert the server port");
        System.out.print(">");
        int port = scanner.nextInt();
        Constants.setAddress(ip);
        Constants.setPort(port);
        CLI cli = new CLI();
        cli.run();
    }

    public void run() {
        setup();
        while (isActiveGame()) {
            input.reset();
//            String cmd = input.nextLine();
            String cmd;
            while ((cmd = input.nextLine()).isEmpty()) {
                System.out.print(cmd + ">");
            }
            listeners.firePropertyChange("action", null, cmd);
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
        if(Objects.equals(evt.getPropertyName(), ServerMessageHandler.GAME_ERROR_LISTER)){
            System.out.println(((GameError) evt.getNewValue()).getMessage());
        }
        else if(Objects.equals(evt.getPropertyName(), ServerMessageHandler.CUSTOM_MESSAGE_LISTER)){
            System.out.println(((CustomMessage) evt.getNewValue()).getMessage());
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
}
