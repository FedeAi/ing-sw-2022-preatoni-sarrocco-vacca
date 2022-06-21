package it.polimi.ingsw.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Magician {
    KING, WITCH, WIZARD, SAGE;

    public static Magician parseMagician(String magician) {
        return Enum.valueOf(Magician.class, magician.toUpperCase());
    }

    public static List<Magician> orderMagicians(List<Magician> old) {
        List<Magician> list = new ArrayList<>(Arrays.stream(Magician.values()).toList());
        return list.stream().filter(element -> old.contains(element)).collect(Collectors.toList());
    }
}
