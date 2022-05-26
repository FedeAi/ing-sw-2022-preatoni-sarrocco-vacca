package it.polimi.ingsw.Constants;

import java.util.*;

public enum Color {
    YELLOW,
    BLUE,
    RED,
    PINK,
    GREEN;


    public static Color parseColor(String color){ return Enum.valueOf(Color.class, color.toUpperCase());}
    // pick random color method
    private static final List<Color> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Color randomColor() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
