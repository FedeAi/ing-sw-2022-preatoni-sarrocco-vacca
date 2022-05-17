package it.polimi.ingsw.Constants;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Islands.IslandContainer;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Printable {

    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PINK = "\u001B[35m";
    static final String RESET = "\u001B[0m";

    /* BRIGHT RED */
    static final String ANSI_BRED = "\u001B[91m";

    static final String emptyRow = "                     ";
    static final String space = "   ";


    public static void printBoard(IslandContainer islandContainer, List<Cloud> clouds) {
        String[] island = {" ▄█████████████████▄ ",
                "██  " + ANSI_RED + "█" + RESET + ":%02d     " + ANSI_YELLOW + "█" + RESET + ":%02d  ██",
                "██  " + ANSI_BLUE + "█" + RESET + ":%02d     " + ANSI_GREEN + "█" + RESET + ":%02d  ██",
                "██  " + ANSI_PINK + "█" + RESET + ":%02d     " + "      ██",
                "██                 ██",
                "██   " + ANSI_BRED + "%s" + RESET + "             ██",
                "██  " + ANSI_BRED + "%s" + RESET + "            ██",
                " ▀█████████████████▀ ",
        };
        String[] cloud = {
                ""
        };
        topRow(island, true);
        midRow(island, false);
        midRow(island, false);
        topRow(island, false);
    }


    private static void topRow(String[] island, boolean motherNature) {
        String[][] islands = new String[4][];
        islands[0] = Arrays.copyOf(island, island.length);
        islands[1] = Arrays.copyOf(island, island.length);
        islands[2] = Arrays.copyOf(island, island.length);
        islands[3] = Arrays.copyOf(island, island.length);


        Map<Color, Integer> map = new EnumMap<>(Color.class);
        map.put(Color.RED, 3);
        map.put(Color.YELLOW, 1);
        map.put(Color.BLUE, 35);
        map.put(Color.GREEN, 31);
        map.put(Color.PINK, 34);


        islands[0] = islandFormat(false, true, islands[0], map);
        islands[1] = islandFormat(false, false, islands[1], map);
        islands[2] = islandFormat(false, false, islands[2], map);
        islands[3] = islandFormat(false, false, islands[3], map);

        for (int i = 0; i < islands[0].length; i++) {
            System.out.print(space);
            System.out.print(islands[0][i]);
            System.out.print(space);
            System.out.print(islands[1][i]);
            System.out.print(space);
            System.out.print(islands[2][i]);
            System.out.print(space);
            System.out.print(islands[3][i]);
            System.out.println();
        }
        System.out.println();
    }

    private static void midRow(String[] island, boolean motherNature) {
        String[][] islands = new String[4][];
        islands[0] = Arrays.copyOf(island, island.length);
        islands[1] = Arrays.copyOf(island, island.length);

        Map<Color, Integer> map = new EnumMap<>(Color.class);
        map.put(Color.RED, 3);
        map.put(Color.YELLOW, 1);
        map.put(Color.BLUE, 35);
        map.put(Color.GREEN, 31);
        map.put(Color.PINK, 34);

        islands[0] = islandFormat(false, false, islands[0], map);
        islands[1] = islandFormat(false, false, islands[1], map);

        for (int i = 0; i < islands[0].length; i++) {
            System.out.print(space);
            System.out.print(islands[0][i]);
            System.out.print(space + space + space + emptyRow + emptyRow);
            System.out.print(islands[1][i]);
            System.out.println();
        }
        System.out.println();
    }

    private static String[] islandFormat(boolean isMerged, boolean motherNature, String island[], Map<Color, Integer> map) {
        String s = "   ";
        if (isMerged) {
            for (int j = 0; j < island.length; j++) {
                island[j] = emptyRow;
            }
        } else {
            island[1] = String.format(island[1], map.get(Color.RED), map.get(Color.YELLOW));
            island[2] = String.format(island[2], map.get(Color.BLUE), map.get(Color.GREEN));
            island[3] = String.format(island[3], map.get(Color.PINK));
            if (motherNature) {
                island[5] = String.format(island[5], "█");
                island[6] = String.format(island[6], "███");
            } else {
                island[5] = String.format(island[5], " ");
                island[6] = String.format(island[6], s);
            }
        }
        return island;
    }

    /*private static String getOwnerColor(Island island) {
        String s = "    ";
        if (island.getOwner() == null) {
            return s;
        } else {
            switch (island.getTowerColor()) {

            }
        }
    }*/
}