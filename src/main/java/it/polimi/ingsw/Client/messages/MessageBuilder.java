package it.polimi.ingsw.Client.messages;

import it.polimi.ingsw.Client.ModelView;
import it.polimi.ingsw.Constants.*;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.CLIColors;


/**
 * MessageBuilder class is useful to build the action (or the message) that client  want to do
 *
 * @author Davide Preatoni, Federico Sarrocco
 */
public class MessageBuilder {

    private ModelView modelView;
    private final String PLAY_CARD_ERROR = "syntax playcard #card (ex: playcard 5) ";
    private final String SETUP_ERROR = "syntax setup error: setup #player #mod (ex: setup 2 expert)";
    private final String MOVE_MOTHER_ERROR = "syntax move mother nature error: movemother #island (ex: movemother 2)";
    private final String CHOOSE_CLOUD_ERROR = "syntax cloud error: cloud #cloud (ex: cloud 2)";
    private final String MOVE_STUDENT_ISLAND_ERROR = "syntax students to island error: studentsisland color #island (ex studentsisland red 3)";
    private final String MOVE_STUDENT_HALL_ERROR = "syntax students to island error: studentshall color #island (ex: studentshall blue 1)";
    private final String ACTIVATE_CARD_ERROR = "syntax activate error: activate #card (ex: activate 1)";
    private final String DEACTIVATE_CARD_ERROR = "syntax deactivate error: deactivate #card (ex:deactivate 2)";
    private final String GRANDMA_ERROR = "syntax grandma error: grandma #island (ex: grandma 2)";
    private final String HERALD_ERROR = "syntax herlad error: herald #island (ex: herald 3)";
    private final String JOKER_ERROR = "syntax joker error: joker color color (ex: joker blue red) ";
    private final String MINSTREL_ERROR = "syntax minstrel error: minstrel color color (ex: minstrel blue red) ";
    private final String MONK_ERROR = "syntax monk error: monk color color (ex: monk blue red) ";
    private final String MUSHROOM_ERROR = "syntax mushroom error: mushroom color (ex: mushroom blue) ";
    private final String PRINCESS_ERROR = "syntax princess error: princess color (ex: princess yellow) ";
    private final String THIEF_ERROR = "syntax thief error: thief color (ex: thief green) ";
    private final String MAGICIAN_ERROR = "syntax magician error: MAGICIAN type (ex: magician king) ";

    public MessageBuilder(ModelView modelView) {
        this.modelView = modelView;
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action playCard(String[] in) {
        try {
            return new Action(ActionType.PLAY_CARD, Integer.parseInt(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + PLAY_CARD_ERROR + CLIColors.RESET); // TODO fire error listener no print
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action chooseMagician(String[] in) {
        try {
            Action actionToSend;
            int magicianIndex = Magician.orderMagicians(modelView.getAvailableMagicians()).indexOf(Magician.parseMagician(in[1].toUpperCase()));
            if (magicianIndex == -1)
                throw new IllegalArgumentException();
            actionToSend = new Action(ActionType.CHOOSE_MAGICIAN, magicianIndex);
            return actionToSend;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MAGICIAN_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action moveMother(String[] in) {
        try {
            return new Action(ActionType.MOVE_MOTHER_NATURE, Integer.parseInt(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MOVE_MOTHER_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action chooseCloud(String[] in) {
        try {
            return new Action(ActionType.CHOOSE_CLOUD, Integer.parseInt(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + CHOOSE_CLOUD_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action moveStudentIsland(String[] in) {
        try {
            return new Action(ActionType.MOVE_STUDENT_ISLAND, Color.parseColor(in[1]), Integer.parseInt(in[2]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MOVE_STUDENT_ISLAND_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action moveStudentHall(String[] in) {
        try {
            return new Action(ActionType.MOVE_STUDENT_HALL, Color.parseColor(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MOVE_STUDENT_HALL_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action activateCard(String[] in) {
        try {
            Action actionToSend;
            actionToSend = new Action(ActionType.ACTIVATE_CARD, Integer.parseInt(in[1]));  // TODO LIKE MAGICIAN name not index
            return actionToSend;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + ACTIVATE_CARD_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action deactivateCard(String[] in) {
        try {
            return new Action(ActionType.DEACTIVATE_CARD, Integer.parseInt(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + DEACTIVATE_CARD_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Message setupMessage(String[] in) {
        try {
            Message messageToSend;
            boolean expertMode = in.length > 2 && in[2].equalsIgnoreCase("expert");
            //modelView.setExpert(expertMode);
            messageToSend = new SetupMessage(Integer.parseInt(in[1]), expertMode);
            return messageToSend;
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + SETUP_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action grandmaBlock(String[] in) {
        try {
            return new Action(ActionType.GRANDMA_BLOCK, Integer.parseInt(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + GRANDMA_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action heraldChoose(String[] in) {
        try {
            return new Action(ActionType.HERALD_CHOOSE, Integer.parseInt(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + HERALD_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action jokerSwap(String[] in) { //FIXME ??
        try {
            return new Action(ActionType.JOKER_SWAP, Color.parseColor(in[1]), Color.parseColor(in[2]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + JOKER_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action minstrelSwap(String[] in) {
        try {
            return new Action(ActionType.MINSTREL_SWAP, Color.parseColor(in[1]), Color.parseColor(in[2]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MINSTREL_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action monkMove(String[] in) {
        try {
            return new Action(ActionType.MONK_MOVE, Color.parseColor(in[1]), Integer.parseInt(in[2]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MONK_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action mushroomChoose(String[] in) {
        try {
            return new Action(ActionType.MUSHROOM_CHOOSE, Color.parseColor(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + MUSHROOM_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action princessMove(String[] in) {
        try {
            return new Action(ActionType.PRINCESS_MOVE, Color.parseColor(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + PRINCESS_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * @param in is the command write by CLI from the user.
     * @return the  appropriate action built
     */
    public Action thiefChoose(String[] in) {
        try {
            return new Action(ActionType.THIEF_CHOOSE, Color.parseColor(in[1]));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + THIEF_ERROR + CLIColors.RESET);
            return null;
        }
    }

    /**
     * 'quit' is used to close the connection
     */
    public Message quit() {
        System.err.println("Disconnected from the server.");
        return new Disconnect();
    }
}
