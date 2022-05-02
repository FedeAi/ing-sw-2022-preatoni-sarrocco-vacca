package it.polimi.ingsw.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

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
