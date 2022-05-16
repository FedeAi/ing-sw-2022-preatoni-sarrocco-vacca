package it.polimi.ingsw.Client.messages;import it.polimi.ingsw.Constants.ActionType;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Constants;

import java.nio.file.NoSuchFileException;
import java.util.Locale;


public class MessageBuilder {

    private final String PLAY_CARD_ERROR = "syntax playcard #card (ex: playcard 5) ";
    private final String SETUP_ERROR = "syntax setup error: setup #player #mod (ex: setup 2 expert)";
    private final String MOVE_MOTHER_ERROR = "syntax move mother nature error: movemother #island (ex: movemother 2)";
    private final String CHOOSE_CLOUD_ERROR = "syntax cloud error: cloud #cloud (ex: cloud 2)";
    private final String MOVE_STUDENT_ISLAND_ERROR = "syntax students to island error: studentsisland color #island (ex studentsisland red 3)";
    private final String MOVE_STUDENT_HALL_ERROR = "syntax students to island error: studentshall color #island (ex: studentshall blue 1)";
    private final String ACTIVATE_CARD_ERROR = "syntax activate error: activate #card (ex: activate 1)";
    private final String DEACTIVATE_CARD_ERROR = "syntax deactivate error: deactivate (ex:deactivate (no needed the number )";
    private final String GRANDMA_ERROR = "syntax grandma error: grandma #island (ex: grandma 2)";
    private final String HERALD_ERROR = "syntax herlad error: herald #island (ex: herald 3)";
    private final String JOKER_ERROR = "syntax joker error: joker color color (ex: joker blue red) ";
    private final String MINSTREL_ERROR = "syntax minstrel error: mintrel color color (ex: minstrel blue red) ";
    private final String MONK_ERROR = "syntax minstrel error: mintrel color color (ex: minstrel blue red) ";
    private final String MUSHROOM_ERROR = "syntax mushroom error: mushroom color (ex: mushroom blue) ";
    private final String PRINCESS_ERROR = "syntax princess error: princess color (ex: princess yellow) ";
    private final String THIEF_ERROR = "syntax thief error: thief color (ex: thief green) ";
    private final String MAGICIAN_ERROR = "syntax thief error: MAGICIAN color (ex: magician 1) ";

    public Action playCard(String[] in){
        try{
            Action actionToSend;
            actionToSend = new Action(ActionType.PLAY_CARD, Integer.parseInt(in[1]));
            return actionToSend;
        }catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(Constants.ANSI_RED + PLAY_CARD_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action chooseMagician(String[] in){
        try{
            Action actionToSend;
            actionToSend = new Action(ActionType.CHOOSE_MAGICIAN, Integer.parseInt(in[1]));
            return actionToSend;
        }catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(Constants.ANSI_RED + MAGICIAN_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action moveMother(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.MOVE_MOTHER_NATURE, Integer.parseInt(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            System.out.println(Constants.ANSI_RED + MOVE_MOTHER_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action chooseCloud(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.CHOOSE_CLOUD, Integer.parseInt(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            System.out.println(Constants.ANSI_RED + CHOOSE_CLOUD_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action moveStudentIsland(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.MOVE_STUDENT_ISLAND, Color.parseColor(in[1]) , Integer.parseInt(in[2]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            System.out.println(Constants.ANSI_RED + MOVE_STUDENT_ISLAND_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action moveStudentHall(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.MOVE_STUDENT_HALL,  Color.parseColor(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            System.out.println(Constants.ANSI_RED + MOVE_STUDENT_HALL_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action activateCard(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.ACTIVATE_CARD,  Integer.parseInt(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            System.out.println(Constants.ANSI_RED + ACTIVATE_CARD_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action deactivateCard(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.DEACTIVATE_CARD);
            return actionToSend;
        }
        catch (IllegalArgumentException e){
            System.out.println(Constants.ANSI_RED + DEACTIVATE_CARD_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Message setupMessage(String[] in) {
        try {
            Message messageToSend;
            messageToSend = new SetupMessage(Integer.parseInt(in[1]), in[2].equalsIgnoreCase("expert"));
            return messageToSend;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(Constants.ANSI_RED + SETUP_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action grandmaBlock(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.GRANDMA_BLOCK,  Integer.parseInt(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED + GRANDMA_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action heraldChoose(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.HERALD_CHOOSE,  Integer.parseInt(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  HERALD_ERROR+ Constants.ANSI_RESET);
            return null;
        }
    }
    public Action jokerSwap(String[] in){ //FIXME
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.JOKER_SWAP,  Color.parseColor(in[1]), Color.parseColor(in[2]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  JOKER_ERROR+ Constants.ANSI_RESET);
            return null;
        }
    }
    public Action minstrelSwap(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.MINSTREL_SWAP,  Color.parseColor(in[1]), Color.parseColor(in[2]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  MINSTREL_ERROR+ Constants.ANSI_RESET);
            return null;
        }
    }
    public Action monkMove(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.MONK_MOVE,  Color.parseColor(in[1]), Integer.parseInt(in[2]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  MONK_ERROR+ Constants.ANSI_RESET);
            return null;
        }
    }
    public Action mushroomChoose(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.MUSHROOM_CHOOSE,  Color.parseColor(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  MUSHROOM_ERROR+ Constants.ANSI_RESET);
            return null;
        }
    }
    public Action princessMove(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.PRINCESS_MOVE,  Color.parseColor(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  PRINCESS_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Action thiefChoose(String[] in){
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.THIEF_CHOOSE,  Color.parseColor(in[1]));
            return actionToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED +  THIEF_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }
    public Message quit() {
        System.err.println("Disconnected from the server.");
        return new Disconnect();
    }



}
