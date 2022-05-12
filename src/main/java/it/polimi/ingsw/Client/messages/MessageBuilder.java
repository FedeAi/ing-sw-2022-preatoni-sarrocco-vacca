package it.polimi.ingsw.Client.messages;import it.polimi.ingsw.Constants.ActionType;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Constants;

import java.nio.file.NoSuchFileException;
import java.util.Locale;

// TODO static?
public class MessageBuilder {
    private final String PLAY_CARD_ERROR = "card not found";
    private final String SETUP_ERROR = "setup error";
    private final String MOVE_MOTHER_ERROR = "position not found";
    private final String CHOOSE_CLOUD_ERROR = "cloud not found";
    private final String MOVE_STUDENT_ISLAND_ERROR = "island/student not found";
    private final String MOVE_STUDENT_HALL_ERROR = "student not found";
    private final String ACTIVATE_CARD_ERROR = "card not found";
    private final String DEACTIVATE_CARD_ERROR = "DEACTIVATE_CARD_ERROR: this should never happen";

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
    public Message setupMessage(String[] in){
        try {
            Message messageToSend;
            messageToSend = new SetupMessage(Integer.parseInt(in[1]), in[2].equalsIgnoreCase("expert"));
            return messageToSend;
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException  e){
            System.out.println(Constants.ANSI_RED + SETUP_ERROR + Constants.ANSI_RESET);
            return null;
        }
    }


    public Message quit() {
        System.err.println("Disconnected from the server.");
        return new Disconnect();
    }



}
