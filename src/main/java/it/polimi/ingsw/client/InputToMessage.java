package it.polimi.ingsw.client;

import it.polimi.ingsw.client.messages.Action;
import it.polimi.ingsw.client.messages.Message;
import it.polimi.ingsw.client.messages.MessageBuilder;
import it.polimi.ingsw.client.messages.PongMessage;
import it.polimi.ingsw.client.messages.turn.EndTurnMessage;
import it.polimi.ingsw.constants.CLIColors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * InputToMessage class parses the Client's commands.
 */
public class InputToMessage implements PropertyChangeListener {

    private ModelView modelView;
    private ConnectionSocket connectionSocket;
    private Action actionToSend;
    private Message messageToSend;

    private MessageBuilder messageBuilder;

    /**
     * Constructor InputToMessage creates a new InputToMessage instance and a new MessageBuilder instance.
     *
     * @param modelView        the client's ModelView instance.
     * @param connectionSocket the client's ConnectionSocket instance.
     * @see InputToMessage
     */
    public InputToMessage(ModelView modelView, ConnectionSocket connectionSocket) {
        this.modelView = modelView;
        this.connectionSocket = connectionSocket;
        messageBuilder = new MessageBuilder(modelView);
    }

    /**
     * Method propertyChange is fired when an Action/Message is ready to be sent.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!modelView.isInputActive()) {
            System.out.println(CLIColors.ANSI_RED + "Error: not your turn!" + CLIColors.RESET);
        } else if (inputParserBuildMessage(evt.getNewValue().toString())) {
            if (actionToSend != null) {
                connectionSocket.send(actionToSend);
            }
            if (messageToSend != null) {
                connectionSocket.send(messageToSend);
            }
            //  modelView.deactivateInput(); ///TODO attivare ogni voltza che riceviamo un messaggio dal server (feedback)
            //commentanto perché testing
        } else {
            modelView.activateInput();
        }
    }

    /**
     * Method inputParserBuildMessage parses the Client's input and
     * calls the relative MessageBuilder method to build the actual message.
     *
     * @param input the Client's input.
     * @return True if the parsing is successful, false otherwise.
     */
    private synchronized boolean inputParserBuildMessage(String input) {
        String[] in = input.split(" ");
        String command = in[0];
        // Action reset
        actionToSend = null;
        // Message reset
        messageToSend = null;
        try {
            switch (command.toUpperCase()) {
                case "MAGICIAN" -> actionToSend = messageBuilder.chooseMagician(in);
                case "PLAYCARD" -> actionToSend = messageBuilder.playCard(in);
                case "CLOUD" -> actionToSend = messageBuilder.chooseCloud(in);
                case "MOVEMOTHER" -> actionToSend = messageBuilder.moveMother(in);
                case "STUDENTISLAND" -> actionToSend = messageBuilder.moveStudentIsland(in);
                case "STUDENTHALL" -> actionToSend = messageBuilder.moveStudentHall(in);
                case "ACTIVATE" -> actionToSend = messageBuilder.activateCard(in);
                case "DEACTIVATE" -> actionToSend = messageBuilder.deactivateCard(in);
                case "SETUP" -> messageToSend = messageBuilder.setupMessage(in);
                case "GRANDMA" -> actionToSend = messageBuilder.grandmaBlock(in);
                case "HERALD" -> actionToSend = messageBuilder.heraldChoose(in);
                case "JOKER" -> actionToSend = messageBuilder.jokerSwap(in);
                case "MINSTREL" -> actionToSend = messageBuilder.minstrelSwap(in);
                case "MONK" -> actionToSend = messageBuilder.monkMove(in);
                case "MUSHROOM" -> actionToSend = messageBuilder.mushroomChoose(in);
                case "PRINCESS" -> actionToSend = messageBuilder.princessMove(in);
                case "THIEF" -> actionToSend = messageBuilder.thiefChoose(in);
                case "PONG" -> messageToSend = new PongMessage();
                case "END" -> messageToSend = new EndTurnMessage();
                case "QUIT" -> {
                    connectionSocket.send(messageBuilder.quit());
                    System.exit(0);
                    return true;
                }
                default -> {
                    System.out.println(CLIColors.ANSI_RED + "404 Command not found" + CLIColors.RESET);
                    return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(CLIColors.ANSI_RED + "Input error; try again!" + CLIColors.RESET);
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Numeric value required, operation not permitted!");
            return false;
        }
        if (actionToSend != null || messageToSend != null) {
            return true;
        }
        return false;
    }
}