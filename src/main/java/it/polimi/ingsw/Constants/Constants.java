package it.polimi.ingsw.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 3;
    public static final int MAX_ISLANDS = 12;
    public static final int INITIAL_BAG_SIZE = 2 * Color.values().length;
    public static final int BAG_SIZE = 130;
    public static final int NUM_CHARACTER_CARDS = 3;
    public static final int NUM_ASSISTANT_CARDS = 10;
    public static final int NUM_COINS = 20;
    public static final int INITIAL_PLAYER_BALANCE = 1;

    public static int port;
    public static String address;

    public static final String ANSI_UNDERLINE = "\033[4m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_GREEN = "\033[32m";
    public static final String ANSI_YELLOW = "\033[33m";
    public static final String ANSI_BLUE = "\033[34m";
    public static final String ANSI_PURPLE = "\033[35m";
    public static final String ANSI_CYAN = "\033[36m";
    public static final String ANSI_WHITE = "\033[37m";
    public static final String ANSI_BACKGROUND_BLACK = "\033[40m";
    public static final String ANSI_BACKGROUND_PURPLE = "\033[45m";
    public static final String ERIANTYS  = "\n" +
            "   ▄████████    ▄████████  ▄█     ▄████████ ███▄▄▄▄       ███     ▄██   ▄      ▄████████ \n" +
            "  ███    ███   ███    ███ ███    ███    ███ ███▀▀▀██▄ ▀█████████▄ ███   ██▄   ███    ███ \n" +
            "  ███    █▀    ███    ███ ███▌   ███    ███ ███   ███    ▀███▀▀██ ███▄▄▄███   ███    █▀  \n" +
            " ▄███▄▄▄      ▄███▄▄▄▄██▀ ███▌   ███    ███ ███   ███     ███   ▀ ▀▀▀▀▀▀███   ███        \n" +
            "▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ███▌ ▀███████████ ███   ███     ███     ▄██   ███ ▀███████████ \n" +
            "  ███    █▄  ▀███████████ ███    ███    ███ ███   ███     ███     ███   ███          ███ \n" +
            "  ███    ███   ███    ███ ███    ███    ███ ███   ███     ███     ███   ███    ▄█    ███ \n" +
            "  ██████████   ███    ███ █▀     ███    █▀   ▀█   █▀     ▄████▀    ▀█████▀   ▄████████▀  \n" +
            "               ███    ███                                                                \n";
    public static final String AUTHORS =
            "\nby "
                    + ANSI_RED
                    + "Davide Preatoni"
                    + ANSI_RESET
                    + ", "
                    + ANSI_CYAN
                    + "Federico Sarrocco"
                    + ANSI_RESET
                    + ", "
                    + ANSI_GREEN
                    + "Alessandro Vacca"
                    + ANSI_RESET;

    /**
     * Method getErr returns the err of this Constants object.
     *
     * @return the err (type String) of this Constants object.
     *
     */
    public static String getErr() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ERR: ");
    }

    /**
     * Method getInfo returns the info of this Constants object.
     *
     * @return the info (type String) of this Constants object.
     */
    public static String getInfo() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " INFO: ");
    }

    public static void setPort(int port) {
        Constants.port = port;
    }

    public int getPort() {
        return port;
    }

    public static void setAddress(String address) {
        Constants.address = address;
    }

    public String getAddress() {
        return address;
    }
}
