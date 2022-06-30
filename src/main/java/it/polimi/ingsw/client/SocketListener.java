package it.polimi.ingsw.client;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.exceptions.InvalidNicknameException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.answers.CustomMessage;
import it.polimi.ingsw.server.answers.SerializedAnswer;
import it.polimi.ingsw.server.answers.WinMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Method SocketListeners listens for a server answer on the socket, passing it to the client model class.
 *
 * @see Runnable
 */
public class SocketListener implements Runnable {

    private final Socket socket;
    private final ModelView modelView;
    private final ServerMessageHandler serverMessageHandler;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ObjectInputStream inputStream;
    private Timer pingWaitingTimer =  new Timer("PingWaitingTimer");

    public static final String CONNECTION_CLOSE_LISTENER = "connectionClosed";


    /**
     * Constructor SocketListener creates a new SocketListener instance.
     *
     * @param socket               of type Socket - socket reference.
     * @param modelView            of type ModelView - modelView reference.
     * @param inputStream          of type ObjectInputStream - the inputStream.
     * @param serverMessageHandler of type ServerMessageHandler - ServerMessageHandler reference.
     */
    public SocketListener(
            Socket socket,
            ModelView modelView,
            ObjectInputStream inputStream,
            ServerMessageHandler serverMessageHandler) {
        this.modelView = modelView;
        this.socket = socket;
        this.inputStream = inputStream;
        this.serverMessageHandler = serverMessageHandler;
    }

    /**
     * Method process elaborates the SerializedAnswer received from the server, passing it to the answerHandler.
     *
     * @param serverMessage of type SerializedAnswer - the serialized answer.
     */
    public void process(SerializedAnswer serverMessage) {
        modelView.setServerAnswer(serverMessage.getServerAnswer());
        serverMessageHandler.answerHandler();
        reStartPingWaitingTimer();
    }

    /**
     * Method reStartPingWaitingTimer restart the pingWaitingTimer, pingWaiting timer is used to established
     * if the connection is still up.
     */
    private void reStartPingWaitingTimer() {
        if(!GameState.getSetupStates().contains(modelView.getGameState())){
            pingWaitingTimer.cancel();
            pingWaitingTimer = new Timer("PingWaitingTimer");

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        socket.close();
                        close();
                        System.out.println("CLOSING THE CLIENT: NOT RECEIVING PING MSGs");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            pingWaitingTimer.schedule(timerTask, (int) (Constants.PING_TIMEOUT_MS * 2));
        }
    }

    /**
     * Method close stop the client
     */
    private void close(){
        if (modelView.getGui() != null) {
            modelView
                    .getGui()
                    .propertyChange(
                            new PropertyChangeEvent(
                                    this, CONNECTION_CLOSE_LISTENER, null, modelView.getServerAnswer().getMessage()));

        } else {
            System.exit(0);
        }
    }

    /**
     * Method run loops and sends all the messages.
     */
    @Override
    public void run() {
        try {
            do {
                SerializedAnswer message = (SerializedAnswer) inputStream.readObject();
                process(message);
            } while (modelView.getCli() == null || modelView.getCli().isActiveGame());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Connection closed by the server. Quitting...");
            close();
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}

