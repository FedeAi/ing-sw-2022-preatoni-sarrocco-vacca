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

    public static List<Color> fromMapToList(Map <Color,Integer> map){
        ArrayList<Color> out = new ArrayList<>();
        for(Map.Entry<Color,Integer> entry : map.entrySet()){
            out.addAll(Collections.nCopies(entry.getValue(), entry.getKey()));
        }
        return out;
    }
}
