package it.polimi.ingsw.Constants;

public enum Magician {
    KING, WIZARD, WITCH, SAGE;

    public static Magician parseMagician(String magician) {
        return Enum.valueOf(Magician.class, magician.toUpperCase());
    }
}
