package it.polimi.ingsw.Constants;

import java.util.*;

/**
 * Class Color represents the game's student colors.
 */
public enum Color {
    YELLOW,
    BLUE,
    RED,
    PINK,
    GREEN;


    /**
     * Method parseColor returns a Color enum instance from the String input.
     *
     * @param color the String that needs to be parsed to Color
     */
    public static Color parseColor(String color) {
        return Enum.valueOf(Color.class, color.toUpperCase());
    }

    // Random color picking method
    private static final List<Color> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Method randomColors returns a random Color instance.
     */
    public static Color randomColor() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    /**
     * Method fromMapToList converts a given Map to a Color List.
     * @param map the Map to be converted to List.
     */
    public static List<Color> fromMapToList(Map<Color, Integer> map) {
        ArrayList<Color> out = new ArrayList<>();
        for (Map.Entry<Color, Integer> entry : map.entrySet()) {
            out.addAll(Collections.nCopies(entry.getValue(), entry.getKey()));
        }
        return out;
    }
}