package it.polimi.ingsw;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.server.Server;

import java.util.Scanner;

/**
 * Eriantys class is main launcher for the project.
 */
public class Eriantys {
    /**
     * Method main lets the user select the different modes of operation: Client (CLI/GUI) or Server.
     *
     * @param args of type String[]
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Eriantys! What type of instance do you want to launch?");
        System.out.println("0 - Server");
        System.out.println("1 - Client with CLI interface");
        System.out.println("2 - Client with GUI interface");

        System.out.print(">");
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        try {
            selection = scanner.nextInt();
        } catch (Exception e) {
            System.err.println("Only numbers are accepted. Exiting...");
            System.exit(1);
        }
        switch (selection) {
            case 0 -> Server.main(null);
            case 1 -> CLI.main(null);
            case 2 -> GUI.main(null);
            default -> {
                System.err.println("Wrong selection. Exiting...");
                try {
                    Thread.sleep(10000);
                    CLI.clearScreen();
                } catch (Exception e) {
                    System.exit(1);
                }
                main(null);
            }
        }
    }
}