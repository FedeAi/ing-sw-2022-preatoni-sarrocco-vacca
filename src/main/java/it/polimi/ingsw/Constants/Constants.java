package it.polimi.ingsw.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 3;
    public static final int MAX_ISLANDS = 12;
    public static final int INITIAL_BAG_SIZE = 2 * Color.values().length;
    public static final int BAG_SIZE = 130;
    public static final int NUM_CHARACTER_CARDS = 3;
    public static final int NUM_ASSISTANT_CARDS = 10;
    public static final int NUM_COINS = 20;
    public static final int INITIAL_PLAYER_BALANCE = 10;
    public static final int SCHOOL_LANE_SIZE = 10;
    public static final int DELAY_WINNING_TIMER = 300;

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
     * Method getErr returns the err of this Constants object.
     *
     * @return the err (type String) of this Constants object.
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

    public static int getPort() {
        return port;
    }

    public static void setAddress(String address) {
        Constants.address = address;
    }

    public static String getAddress() {
        return address;
    }

    /**
     * input validation method: true if the string has matched with the pattern of Regex
     */
    public static boolean validateIp(String ip) {

        boolean validate = false;
        ip = ip.toLowerCase();
        Pattern p = Pattern.compile("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){3}\\b"); //pattern for validate the ip address
        Matcher m = p.matcher(ip);
        if (m.matches() || ip.equals("localhost")) {
            validate = true;
        }
        return validate;

    }

    /**
     * input validation method: true if the string has matched with the pattern of Regex
     *
     * @return integer because we passed a string (the pattern of regex required a string) and after we cast the port
     */
    public static int validatePort(String Port) {

        Pattern p = Pattern.compile("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$"); //pattern tto validate port
        Matcher m = p.matcher(Port);
        try {
            int port = Integer.parseInt(Port);
        } catch(Exception e) {
            return -1;
        }
        if (!(port > 1023 && m.matches())) { // below 1023 the are ports reserved for OS
            return -1;
        }
        return port;
    }

    /**
     * input validation method: true if the string has matched with the pattern of Regex
     */
    public static boolean validateNickname(String user) {
        boolean validate = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){2,18}[a-zA-Z0-9]$");
        Matcher m = p.matcher(user);
        if (m.matches()) {
            validate = true;
        }
        return validate;
    }

}
