package it.polimi.ingsw.Constants;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Islands.IslandContainer;
import it.polimi.ingsw.Model.Islands.SuperIsland;
import it.polimi.ingsw.Model.School;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printable {

    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PINK = "\u001B[35m";
    static final String RESET = "\u001B[0m";
    /* BRIGHT RED */
    static final String ANSI_BRED = "\u001B[91m";
    static final String ANSI_WHITE = "\u001B[37m";
    static final String ANSI_GREY = "\u001B[90m";

    static final String emptyRow = "                     ";
    static final String space = "   ";


    public static void printBoard(IslandContainer islandContainer, int mn, List<String> players, Map<String, School> schoolMap) {
        String[] island = {" ▄█████████████████▄ ",
                "██  " + ANSI_RED + "█" + RESET + ":%02d     " + ANSI_YELLOW + "█" + RESET + ":%02d  ██",
                "██  " + ANSI_BLUE + "█" + RESET + ":%02d     " + ANSI_GREEN + "█" + RESET + ":%02d  ██",
                "██  " + ANSI_PINK + "█" + RESET + ":%02d     " + "      ██",
                "██                 ██",
                "██   " + ANSI_BRED + "%s" + RESET + "  %s  ██",
                "██  " + ANSI_BRED + "%s" + RESET + "   ♜:%01d   %02d ██",
                " ▀█████████████████▀ ",
        };
        Map<String, TowerColor> map = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            String p = players.get(i);
            map.put(p, schoolMap.get(p).getTowerColor());
        }
        String[][] islands = prepareIslands(island, islandContainer, mn, map);
        topRow(islands);
        topMidRow(islands);
        bottomMidRow(islands);
        bottomRow(islands);
    }

    private static void topRow(String[][] islands) {
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

    private static void topMidRow(String[][] islands) {
        for (int i = 0; i < islands[0].length; i++) {
            System.out.print(space);
            System.out.print(islands[11][i]);
            System.out.print(space + space + space + emptyRow + emptyRow);
            System.out.print(islands[4][i]);
            System.out.println();
        }
        System.out.println();
    }

    private static void bottomMidRow(String[][] islands) {
        for (int i = 0; i < islands[0].length; i++) {
            System.out.print(space);
            System.out.print(islands[10][i]);
            System.out.print(space + space + space + emptyRow + emptyRow);
            System.out.print(islands[5][i]);
            System.out.println();
        }
        System.out.println();
    }

    private static void bottomRow(String[][] islands) {
        for (int i = 0; i < islands[0].length; i++) {
            System.out.print(space);
            System.out.print(islands[9][i]);
            System.out.print(space);
            System.out.print(islands[8][i]);
            System.out.print(space);
            System.out.print(islands[7][i]);
            System.out.print(space);
            System.out.print(islands[6][i]);
            System.out.println();
        }
        System.out.println();
    }

    private static String[] islandFormat(boolean isMerged, boolean motherNature, String pIsland[], Map<Color, Integer> map, int towers, String towerColor, int n) {
        String[] island = Arrays.copyOf(pIsland, pIsland.length);
        String s = "   ";
        if (isMerged) {
            Arrays.fill(island, emptyRow);
        } else {
            island[1] = String.format(island[1], map.getOrDefault(Color.RED, 0), map.getOrDefault(Color.YELLOW, 0));
            island[2] = String.format(island[2], map.getOrDefault(Color.BLUE, 0), map.getOrDefault(Color.GREEN, 0));
            island[3] = String.format(island[3], map.getOrDefault(Color.PINK, 0));
            if (motherNature) {
                island[5] = String.format(island[5], "█", towerColor);
                island[6] = String.format(island[6], "███", towers, n);
            } else {
                island[5] = String.format(island[5], " ", towerColor);
                island[6] = String.format(island[6], s, towers, n);
            }
        }
        return island;
    }

    private static String[][] prepareIslands(String[] island, IslandContainer islandContainer, int mn, Map<String, TowerColor> map) {
        String[][] stringIslands = new String[12][];
        int k = 0;
        boolean motherNature;
        for (int i = 0; i < islandContainer.size(); i++, k++) {
            motherNature = false;
            Island isl = islandContainer.get(i);
            if (mn == i) {
                motherNature = true;
            }
            if (isl instanceof SuperIsland) {
                int n = isl.size();
                stringIslands[k] = islandFormat(false, motherNature, island, isl.getStudents(), n, getOwnerColor(isl.getOwner(), map), i);
                for (int j = 0; j < n - 1; j++) {
                    k++;
                    stringIslands[k] = islandFormat(true, false, island, isl.getStudents(), 0, getOwnerColor(null, null), 0);
                }
            } else {
                int towers = getOwnerColor(isl.getOwner(), map).equals("         ") ? 0 : 1;
                stringIslands[k] = islandFormat(false, motherNature, island, isl.getStudents(), towers, getOwnerColor(isl.getOwner(), map), i);
            }
        }
        return stringIslands;
    }

    private static String getOwnerColor(String owner, Map<String, TowerColor> map) {
        String s = "         ";
        if (owner == null) {
            return s;
        } else {
            return "  " + map.get(owner).toString() + "  ";
        }
    }

    public static void printClouds(List<Cloud> cloudContainer) {
        String[] cloud = {" ▄█████████████████▄ ",
                "██  " + ANSI_RED + "█" + RESET + ":%02d     " + ANSI_YELLOW + "█" + RESET + ":%02d  ██",
                "██  " + ANSI_BLUE + "█" + RESET + ":%02d     " + ANSI_GREEN + "█" + RESET + ":%02d  ██",
                "██  " + ANSI_PINK + "█" + RESET + ":%02d     " + "    %01d ██",
                " ▀█████████████████▀ ",
        };

        String[][] clouds = prepareClouds(cloudContainer, cloud);
        for (int i = 0; i < clouds[0].length; i++) {
            System.out.print(space);
            System.out.print(clouds[0][i]);
            System.out.print(space);
            System.out.print(clouds[1][i]);
            System.out.print(space);
            System.out.print(clouds[2][i]);
            System.out.println();
        }
    }

    private static String[][] prepareClouds(List<Cloud> clouds, String[] cloud) {
        String[][] preparedClouds = new String[3][];
        int i = 0;
        boolean hidden = false;
        while (i < 3) {
            if (clouds.get(i).isEmpty()) {
                preparedClouds[i] = cloudFormat(true, cloud, clouds.get(i).getStudents(), i);
            } else {
                preparedClouds[i] = cloudFormat(hidden, cloud, clouds.get(i).getStudents(), i);
            }
            if (i == 1 && i == clouds.size() - 1) {
                preparedClouds[2] = cloudFormat(true, cloud, clouds.get(0).getStudents(), i);
                break;
            }
            i++;
        }
        return preparedClouds;
    }

    private static String[] cloudFormat(boolean isHidden, String[] cloud, Map<Color, Integer> map, int n) {
        String[] formattedCloud = Arrays.copyOf(cloud, cloud.length);
        if (isHidden) {
            Arrays.fill(formattedCloud, emptyRow);
        } else {
            formattedCloud[1] = String.format(formattedCloud[1], map.getOrDefault(Color.RED, 0), map.getOrDefault(Color.YELLOW, 0));
            formattedCloud[2] = String.format(formattedCloud[2], map.getOrDefault(Color.BLUE, 0), map.getOrDefault(Color.GREEN, 0));
            formattedCloud[3] = String.format(formattedCloud[3], map.getOrDefault(Color.PINK, 0), n);
        }
        return formattedCloud;
    }

    public static void printSchool(School school) {
        String[] schoolBoard = {"█████████████████████████████████████████",
                "██   ENTRY  " + " " + "██" + "   HALL   " + "██" + "   TOWERS   " + "██",
                "██   " + ANSI_GREEN + "█" + RESET + ":%02d    " + "██" + "   " + ANSI_GREEN + "█" + RESET + ":%02d   " + "██" + "    " + "♜:%01d" + "     " + "██",
                "██   " + ANSI_RED + "█" + RESET + ":%02d    " + "██" + "   " + ANSI_RED + "█" + RESET + ":%02d   " + "██" + "            " + "██",
                "██   " + ANSI_YELLOW + "█" + RESET + ":%02d    " + "██" + "   " + ANSI_YELLOW + "█" + RESET + ":%02d   " + "██" + "            " + "██",
                "██   " + ANSI_PINK + "█" + RESET + ":%02d    " + "██" + "   " + ANSI_PINK + "█" + RESET + ":%02d   " + "██" + "   COLOR:   " + "██",
                "██   " + ANSI_BLUE + "█" + RESET + ":%02d    " + "██" + "   " + ANSI_BLUE + "█" + RESET + ":%02d   " + "██" + "   %s    " + "██",
                "█████████████████████████████████████████",
        };

        schoolBoard = prepareSchool(schoolBoard, school.getStudentsEntry(), school.getStudentsHall(), school.getNumTowers(), school.getTowerColor());

        for (String s : schoolBoard) {
            System.out.print(space);
            System.out.print(s);
            System.out.println();
        }
    }

    private static String[] prepareSchool(String[] schoolTemplate, Map<Color, Integer> entry, Map<Color, Integer> hall, int towers, TowerColor tc) {
        String[] school = Arrays.copyOf(schoolTemplate, schoolTemplate.length);
        Color color = Color.GREEN;
        String s;
        if (tc == TowerColor.GRAY) {
            s = "GRAY ";
        } else {
            s = tc.toString();
        }
        school[2] = String.format(school[2], entry.getOrDefault(color, 0), hall.getOrDefault(color, 0), towers);
        color = Color.RED;
        school[3] = String.format(school[3], entry.getOrDefault(color, 0), hall.getOrDefault(color, 0));
        color = Color.YELLOW;
        school[4] = String.format(school[4], entry.getOrDefault(color, 0), hall.getOrDefault(color, 0));
        color = Color.PINK;
        school[5] = String.format(school[5], entry.getOrDefault(color, 0), hall.getOrDefault(color, 0));
        color = Color.BLUE;
        school[6] = String.format(school[6], entry.getOrDefault(color, 0), hall.getOrDefault(color, 0), s);
        return school;
    }
}