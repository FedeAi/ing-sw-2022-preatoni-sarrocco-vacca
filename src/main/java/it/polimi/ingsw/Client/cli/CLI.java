package it.polimi.ingsw.Client.cli;

import it.polimi.ingsw.Client.ActionHandler;
import it.polimi.ingsw.Client.ConnectionSocket;
import it.polimi.ingsw.Client.ModelView;
import it.polimi.ingsw.Client.UI;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Logger;

public class CLI implements UI {

    private static final Logger logger = Logger.getLogger(CLI.class.getName());
    private final PrintStream output;
    private final Scanner input;
    private final ModelView modelView;
    private final ActionHandler actionHandler;
    private boolean activeGame;

    private ConnectionSocket connectionSocket;

    public CLI() {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        modelView = new ModelView(this);
        actionHandler = new ActionHandler(this, modelView);
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
//        while (isActiveGame()) {
//            loop();
//        }
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
            if (!connectionSocket.setup(nickname, modelView, actionHandler)) {
                System.err.println("The entered IP/port doesn't match any active server or the server is not " +
                        "running. Please try again!");
                CLI.main(null);
            }
            System.out.println(/*nameMapColor.get(GREEN)+ */  "Socket Connection setup completed!" /* + nameMapColor.get("RST")*/);
        } catch (DuplicateNicknameException | InvalidNicknameException e) {
            setup();
        }
        //listeners.addPropertyChangeListener("action", new ActionParser(connectionSocket, modelView));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public boolean isActiveGame() {
        return activeGame;
    }
}
