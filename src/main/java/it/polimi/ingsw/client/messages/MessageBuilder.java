package it.polimi.ingsw.client.messages;

import it.polimi.ingsw.client.ModelView;
import it.polimi.ingsw.constants.*;
import it.polimi.ingsw.constants.Color;
import it.polimi.ingsw.constants.CLIColors;


/**
 * MessageBuilder class creates the Message or Action selected by the Client.
 *
 * @author Davide Preatoni, Federico Sarrocco
 */
public class MessageBuilder {

    private ModelView modelView;
    private final String PLAY_CARD_ERROR = "Wrong PlayCard syntax!\nValid syntax: playcard #card (e.g. playcard 5)";
    private final String SETUP_ERROR = "Wrong Setup syntax!\nValid syntax: setup #players ?mode (e.g. setup 2 expert)";
    private final String MOVE_MOTHER_ERROR = "Wrong MoveMotherNature syntax!\nValid syntax: movemother #island (e.g. movemother 2)";
    private final String CHOOSE_CLOUD_ERROR = "Wrong ChooseCloud syntax!\nValid syntax: cloud #index (ex: cloud 2)";
    private final String MOVE_STUDENT_ISLAND_ERROR = "Wrong MoveStudentToIsland syntax!\nValid syntax: studentisland color #island (e.g. studentisland red 3)";
    private final String MOVE_STUDENT_HALL_ERROR = "Wrong MoveStudentToHall syntax!\nValid syntax: studenthall color (e.g. studentshall blue)";
    private final String ACTIVATE_CARD_ERROR = "Wrong ActivateCard syntax!\nValid syntax: activate #card (e.g. activate 1)";
    private final String DEACTIVATE_CARD_ERROR = "Wrong DeactivateCard syntax!\nValid syntax: deactivate #card (e.g. deactivate 2)";
    private final String GRANDMA_ERROR = "Wrong GrandmaBlockIsland syntax!\nValid syntax: grandma #island (e.g. grandma 2)";
    private final String HERALD_ERROR = "Wrong HeraldChooseIsland syntax!\nValid syntax: herald #island (e.g. herald 3)";
    private final String JOKER_ERROR = "Wrong JokerSwapStudents syntax!\nValid syntax: joker color color (e.g. joker blue red)";
    private final String MINSTREL_ERROR = "Wrong MinstrelSwapStudents syntax!\nValid syntax: minstrel color color (e.g. minstrel blue red) ";
    private final String MONK_ERROR = "Wrong MonkMoveToIsland syntax!\nValid syntax: monk color color (e.g. monk blue red)";
    private final String MUSHROOM_ERROR = "Wrong MushroomChooseColor syntax!\nValid syntax: mushroom color (e.g. mushroom blue)";
    private final String PRINCESS_ERROR = "Wrong PrincessMoveToEntry syntax!\nValid syntax: princess color (e.g. princess yellow)";
    private final String THIEF_ERROR = "Wrong ThiefChooseColor syntax!\nValid syntax: thief color (e.g. thief green)";
    private final String MAGICIAN_ERROR = "Wrong ChooseMagician syntax!\nValid syntax: magician type (e.g. magician king)";

    /**
     * Constructor MessageBuilder creates a new MessageBuilder instance.
     *
     * @param modelView the Client's model for visualization.
     */
    public MessageBuilder(ModelView modelView) {
        this.modelView = modelView;
    }

    /**
     * Method playCard creates a PlayCard type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method chooseMagician creates a ChooseMagician type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
     */
    public Action chooseMagician(String[] in) {
        try {
            Action actionToSend;
            int magicianIndex = modelView.getAvailableMagicians().indexOf(Magician.parseMagician(in[1].toUpperCase()));
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
     * Method moveMother creates a MoveMotherNature type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method chooseCloud creates a ChooseCloud type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method moveStudentIsland creates a MoveStudentToIsland type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method moveStudentHall creates a MoveStudentToHall type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method activateCard creates a ActivateCard type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method deactivateCard creates a DeactivateCard type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method setupMessage creates a SetupMessage type of Message.
     *
     * @param in is the user written action in the terminal.
     * @return The created Message if successful, null otherwise.
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
     * Method grandmaBlock creates a GrandmaBlockIsland type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method heraldChoose creates a HeraldChooseIsland type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method jokerSwap creates a JokerSwapStudents type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method minstrelSwap creates a MinstrelSwapStudents type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method monkMove creates a MonkMoveToIsland type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method mushroomChoose creates a MushroomChooseColor type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method princessMove creates a PrincessMoveToHall type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method thiefChoose creates a ThiefChooseColor type of Action.
     *
     * @param in is the user written action in the terminal.
     * @return The created Action if successful, null otherwise.
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
     * Method quit creates a new Disconnect type of Message.
     * @return the Disconnect message.
     */
    public Message quit() {
        System.err.println("Disconnected from the server.");
        return new Disconnect();
    }
}