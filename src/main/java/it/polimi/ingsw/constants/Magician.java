package it.polimi.ingsw.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class Magician represents all the Magicians present in the game.
 */
public enum Magician {

    KING, WITCH, WIZARD, SAGE;

    /**
     * Method parseMagician returns a Magician enum instance from the String input.
     *
     * @param magician the String that needs to be parsed to Magician
     */
    public static Magician parseMagician(String magician) {
        return Enum.valueOf(Magician.class, magician.toUpperCase());
    }

    /**
     * Method orderMagicians returns the ordered Magician (order defined by the enum declaration).
     * @param old the unordered list.
     * @return The ordered list.
     */
    public static List<Magician> orderMagicians(List<Magician> old) {
        List<Magician> list = new ArrayList<>(Arrays.stream(Magician.values()).toList());
        return list.stream().filter(element -> old.contains(element)).collect(Collectors.toList());
    }
}
