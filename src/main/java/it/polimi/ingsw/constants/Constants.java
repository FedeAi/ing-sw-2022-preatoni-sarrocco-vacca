package it.polimi.ingsw.constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Constants represents all the game's game constraints, and it also contains the network address and port.
 */
public class Constants {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 3;
    public static final int MAX_ISLANDS = 12;
    public static final int INITIAL_BAG_SIZE = 2 * Color.values().length;
    public static final int BAG_SIZE = 130;
    public static final int NUM_CHARACTER_CARDS = 3;
    public static final int NUM_ASSISTANT_CARDS = 10;
    public static final int NUM_COINS = 200; // TODO MUST BE 20 ALSO THE PLAYERBALANCE
    public static final int INITIAL_PLAYER_BALANCE = 10;
    public static final int SCHOOL_LANE_SIZE = 10;
    public static final int DELAY_WINNING_TIMER = 120;
    public static final int PING_TIMEOUT_MS = 5000;

    public static int port = 8080;
    public static String address;

    public static final String ERIANTYS = "\n" +
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
                    + CLIColors.ANSI_RED
                    + "Davide Preatoni"
                    + CLIColors.RESET
                    + ", "
                    + CLIColors.ANSI_CYAN
                    + "Federico Sarrocco"
                    + CLIColors.RESET
                    + ", "
                    + CLIColors.ANSI_GREEN
                    + "Alessandro Vacca"
                    + CLIColors.RESET;

    /**
     * Method getErr returns the error of this Constants object.
     */
    public static String getErr() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ERR: ");
    }

    /**
     * Method getInfo returns the info of this Constants object.
     */
    public static String getInfo() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " INFO: ");
    }

    /**
     * Method setAddress sets a new port.
     *
     * @param port the new port to be set.
     */
    public static void setPort(int port) {
        Constants.port = port;
    }

    /**
     * Method getAddress returns the set port.
     */
    public static int getPort() {
        return port;
    }

    /**
     * Method setAddress sets a new IP address.
     *
     * @param address the new IP address to be set.
     */
    public static void setAddress(String address) {
        Constants.address = address;
    }

    /**
     * Method getAddress returns the set IP address.
     */
    public static String getAddress() {
        return address;
    }

    /**
     * Method validateIP checks for the correctness of a given IP address with a regular expression.
     *
     * @return true if the given ip is valid, false otherwise.
     */
    public static boolean validateIP(String ip) {
        boolean validate = false;
        ip = ip.toLowerCase();
        Pattern p = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$"); //pattern for validate the ip address
        Matcher m = p.matcher(ip);
        if (m.matches() || ip.equals("localhost")) {
            validate = true;
        }
        return validate;
    }

    /**
     * Method validatePort checks for the correctness of a given port with a regular expression.
     *
     * @returns the chosen port if valid, -1 otherwise.
     */
    public static int validatePort(String Port) {
        // REGEX pattern for input port validation.
        Pattern p = Pattern.compile(
                "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
        Matcher m = p.matcher(Port);
        try {
            int port = Integer.parseInt(Port);
        } catch (Exception e) {
            return -1;
        }
        // If a port is below 1024 it's not valid.
        if (!(port > 1023 && m.matches())) {
            return -1;
        }
        return port;
    }

    /**
     * Method validateNickname checks for the correctness of a nickname with a regular expression.
     *
     * @return true if the string has matched with the pattern of the regex, false otherwise.
     */
    public static boolean validateNickname(String user) {
        boolean validate = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,14}[a-zA-Z0-9]$");
        Matcher m = p.matcher(user);
        if (m.matches()) {
            validate = true;
        }
        return validate;
    }
}