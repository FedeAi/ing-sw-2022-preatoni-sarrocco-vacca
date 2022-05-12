package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.messages.Action;
import it.polimi.ingsw.Client.messages.LoginMessage;
import it.polimi.ingsw.Client.messages.Message;
import it.polimi.ingsw.Client.messages.MessageBuilder;
import it.polimi.ingsw.Client.messages.turn.EndTurnMessage;
import it.polimi.ingsw.Constants.Constants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ActionBuilder class builds messages
 */
public class InputToMessage implements PropertyChangeListener {

    private ModelView modelView;
    private ConnectionSocket connectionSocket;
    private Action actionToSend;
    private Message messageToSend;

    private MessageBuilder messageBuilder;

    public InputToMessage(ModelView modelView, ConnectionSocket connectionSocket){
        this.modelView = modelView;
        this.connectionSocket = connectionSocket;
        messageBuilder = new MessageBuilder();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!modelView.isInputActive()) {
            System.out.println(Constants.ANSI_RED + "Error: not your turn!" + Constants.ANSI_RESET);
        } else if (inputParserBuildMessage(evt.getNewValue().toString())) {
            if(actionToSend != null ) {
                connectionSocket.send(actionToSend);
            }
            if(messageToSend != null) {
                connectionSocket.send(messageToSend);
            }
          //  modelView.deactivateInput(); ///TODO attivare ogni voltza che riceviamo un messaggio dal server (feedback)
            //commentanto perché testing
        } else {
            modelView.activateInput();
        }
    }


    private synchronized boolean inputParserBuildMessage(String input) {
        String[] in = input.split(" ");
        String command = in[0];
        actionToSend = null; // reset of action
        messageToSend = null; //reset of message

        try {
            switch (command.toUpperCase()) {

                case "PLAYCARD" -> actionToSend = messageBuilder.playCard(in);
                case "CHOOSECLOUD" -> actionToSend = messageBuilder.chooseCloud(in);
                case "MOVEMOTHER" -> actionToSend = messageBuilder.moveMother(in);
                case "MOVESTUDENTISLAND" -> actionToSend = messageBuilder.moveStudentIsland(in);
                case "MOVESTUDENTHAL"-> actionToSend = messageBuilder.moveStudentHall(in);
                case "ACTIVATECARD"-> actionToSend = messageBuilder.activateCard(in);
                case "DEACTIVATECARD"-> actionToSend = messageBuilder.deactivateCard(in);
                case "SETUP" -> messageToSend = messageBuilder.setupMessage(in);
//                case "" -> sendMessage = inputChecker.desc(in);
//                case "" -> sendMessage = inputChecker.addGod(in);
//                case "" -> sendMessage = checkSelectWorker(inx
//                case "" -> sendMessage = checkMove(in, modelView.getTurnPhase());
//                case "" -> sendMessage = checkBuild(in, modelView.getTurnPhase());
//                case "" -> sendMessage = checkPlaceDome(in, modelView.getTurnPhase());
//                case "" -> sendMessage = checkForceWorker(in, modelView.getTurnPhase());
//                case "" -> sendMessage = checkRemoveLevel(in, modelView.getTurnPhase());
                case "END" -> messageToSend = new EndTurnMessage();
                case "QUIT" -> {
                    connectionSocket.send(messageBuilder.quit());
                    System.exit(0);
                    return true;
                }
                default -> {
                    System.out.println(Constants.ANSI_RED + "404 Command not found" + Constants.ANSI_RESET);
                    return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(Constants.ANSI_RED + "Input error; try again!" + Constants.ANSI_RESET);
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Numeric value required, operation not permitted!");
            return false;
        }
        if (actionToSend != null || messageToSend !=null) {
            return true;
        }
        return false;
    }
}

