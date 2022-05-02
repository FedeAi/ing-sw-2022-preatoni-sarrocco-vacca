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

}
