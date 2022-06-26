package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.client.messages.*;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.ErrorType;
import it.polimi.ingsw.server.answers.*;
import it.polimi.ingsw.controller.actions.*;
import it.polimi.ingsw.controller.actions.characters.*;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.InvalidPlayerException;
import it.polimi.ingsw.exceptions.RoundOwnerException;

/**
 * SocketClientConnection handles a connection between client and server, permitting sending and
 * receiving messages and doing other class-useful operations too.
 *
 * @author Davide Preatoni, Federico Sarrocco, Alessandro Vacca
 * @see Runnable
 * @see ClientConnection
 */
public class SocketClientConnection implements ClientConnection, Runnable {
    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Integer clientID;
    private boolean active;
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Method isActive returns the active of this SocketClientConnection object.
     *
     * @return the active (type boolean) of this SocketClientConnection object.
     */
    public boolean isActive() {
        return !getSocket().isClosed();
//        return active;
    }

    /**
     * Constructor SocketClientConnection instantiates an input/output stream from the socket received
     * as parameters, and adds the main server to his attributes too.
     *
     * @param socket of type Socket - the socket which accepted the client connection.
     * @param server of type Server - the main server class.
     */
    public SocketClientConnection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            clientID = -1;
            active = true;
        } catch (IOException e) {
            System.err.println(Constants.getErr() + "Error socket-stream!");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Method getSocket returns the socket of this SocketClientConnection object.
     *
     * @return the socket (type Socket) of this SocketClientConnection object.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Method close terminates the connection with the client, closing firstly input and output
     * streams, then invoking the server method called "unregisterClient", which will remove the
     * active virtual client from the list.
     *
     * @see it.polimi.ingsw.server.Server#unregisterClient for more details.
     */
    public void close() {
        active = false;
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        server.unregisterClient(this.getClientID(), server.getGameByID(clientID).isEnded() || !server.getGameByID(clientID).isStarted() || server.getGameByID(clientID).isSetupPhase());
    }

    /**
     * Method readFromStream reads a serializable object from the input stream, using
     * ObjectInputStream library.
     *
     * @throws IOException            when the client is not online anymore.
     * @throws ClassNotFoundException when the serializable object is not part of any class.
     */
    public synchronized void readFromStream() throws IOException, ClassNotFoundException {    // TODO
        SerializedMessage input = (SerializedMessage) inputStream.readObject();
        if (input.message != null) {
            Message command = input.message;
            actionHandler(command);
        } else if (input.action != null) {
            Action action = input.action;
            actionHandler(action);
        }
    }

    /**
     * Method run is the overriding runnable class method, which is called on a new client connection.
     *
     * @see Runnable#run()
     */
    @Override
    public void run() {
        try {
            while (isActive()) {
                readFromStream();
            }
        } catch (IOException e) {
            GameHandler game = server.getGameByID(clientID);
            String player = server.getNicknameByID(clientID);
            close();
            // FIXME
//            server.unregisterClient(clientID);
//            if (game.isStarted()) {
//                game.endGame(player);
//            }
            System.err.println(Constants.getInfo() + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Method actionHandler handles an action by receiving a message from the client.
     *
     * @param command of type Message - the Message interface type command, which needs to be checked
     *                in order to perform an action.
     */
    public void actionHandler(Message command) {
        System.out.println("Debug: MESSAGE RECEIVED: " + command.getClass().getName());
        if (command instanceof LoginMessage) {
            setupConnection((LoginMessage) command);
        }
    }

    /**
     * Method actionHandler handles an action by parsing from what it's received an action from the client.
     *
     * @param action the generic action to be executed.
     */
    public void actionHandler(Action action) {
        System.out.println("Debug: ACTION RECEIVED: " + action.actionType.name());
        Performable move;
        /*if (command instanceof LoginMessage) {
            setupConnection((LoginMessage) command);
        }*/
        String nickname = server.getNicknameByID(clientID);
        switch (action.actionType) {
            case CHOOSE_MAGICIAN -> move = new ChooseMagician(nickname, action.int0);
            case PLAY_CARD -> move = new PlayCard(nickname, action.int0);
            case MOVE_MOTHER_NATURE -> move = new MoveMotherNature(nickname, action.int0);
            case MOVE_STUDENT_ISLAND -> move = new MoveStudentFromEntryToIsland(nickname, action.color0, action.int0);
            case MOVE_STUDENT_HALL -> move = new MoveStudentFromEntryToHall(nickname, action.color0);
            case CHOOSE_CLOUD -> move = new ChooseCloud(nickname, action.int0);
            case ACTIVATE_CARD -> move = new ActivateCard(nickname, action.int0);
            case DEACTIVATE_CARD -> move = new DeactivateCard(nickname, action.int0);
            case GRANDMA_BLOCK -> move = new GrandmaBlockIsland(nickname, action.int0);
            case HERALD_CHOOSE -> move = new HeraldChooseIsland(nickname, action.int0);
            case JOKER_SWAP -> move = new JokerSwapStudents(nickname, action.color0, action.color1);
            case MINSTREL_SWAP -> move = new MinstrelSwapStudents(nickname, action.color0, action.color1);
            case MONK_MOVE -> move = new MonkMoveToIsland(nickname, action.color0, action.int0);
            case MUSHROOM_CHOOSE -> move = new MushroomChooseColor(nickname, action.color0);
            case PRINCESS_MOVE -> move = new PrincessMoveToHall(nickname, action.color0);
            case THIEF_CHOOSE -> move = new ThiefChooseColor(nickname, action.color0);
            default -> {
                server.getClientByID(clientID).send(new GameError(ErrorType.INVALID_MOVE, "Specified move is badly formatted."));
                return;
            }
        }
        try {
            server.getGameByID(clientID).performAction(move);
        } catch (GameException | InvalidPlayerException | RoundOwnerException e) {
            server.getClientByID(clientID).send(new GameError(e.getMessage()));
        }
    }

    /**
     * Method setupConnection checks the validity of the connection message received from the client.
     *
     * @param command of type SetupConnection - the connection command.
     */
    private void setupConnection(LoginMessage command) {
        if (!server.isNicknameTaken(command.getNickname())) {
            try {
                clientID = server.registerNewConnection(command.getNickname(), this);
                if (clientID == null) {
                    active = false;
                    return;
                }
                server.lobby(this);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }

        } else {
            clientID = server.recoverConnection(command.getNickname(), this);
            if (clientID != null) {
                return;
            }
        }
    }


    /**
     * Method setPlayers is a setup method. It permits setting the number of the players in the match,
     * which is decided by the first user connected to the server. It waits for a ReqPlayersMessage
     * Message type, then extracts the information about the number of players, passing it as a
     * parameter to the server function "setTotalPlayers".
     *
     * @param message of type RequestPlayersNumber - the action received from the user. This method
     *                iterates on it until it finds a NumberOfPlayers type.
     */
    public void setup(ReqPlayersMessage message) {
        SerializedAnswer ans = new SerializedAnswer();
        ans.setServerAnswer(message);
        sendSocketMessage(ans);
        while (true) {
            try {
                SerializedMessage input = (SerializedMessage) inputStream.readObject();
                Message command = input.message;
                if (command instanceof SetupMessage) {
                    try {
                        int playerNumber = (((SetupMessage) command).playersNumber);
                        boolean expertMode = (((SetupMessage) command).expertMode);
                        server.setTotalPlayers(playerNumber);
                        server.getGameByID(clientID).setPlayersNumber(playerNumber);
                        server.getGameByID(clientID).setExpertMode(expertMode);
                        server.getClientByID(this.clientID).send(new CustomMessage("Success: player number " + "set to " + playerNumber + ", game mode: " + (expertMode ? "expert" : "normal")));
                        server.getClientByID(this.clientID).send(new CustomMessage("Waiting for the other players..."));
                        break;
                        // FIXME custom exception
                    } catch (Exception e) {
                        server.getClientByID(this.clientID).send(new CustomMessage("Error: not a valid " + "input! Please provide a value of 2 or 3."));
                        server.getClientByID(this.clientID).send(new ReqPlayersMessage("Choose the number" + " of players!\n setup [2/3] [?expert]"));
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                close();
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Method sendSocketMessage allows dispatching the server's Answer to the correct client. The type
     * SerializedMessage contains an Answer type object, which represents an interface for server
     * answer, like the client Message one.
     *
     * @param serverAnswer of type SerializedAnswer - the serialized server answer (interface Answer).
     */
    public void sendSocketMessage(SerializedAnswer serverAnswer) {
        try {
            System.out.println(serverAnswer.getServerAnswer().getClass());
            outputStream.reset();
            outputStream.writeObject(serverAnswer);
            outputStream.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            close();
        }
    }

    /**
     * Method ping allows the server to check if a client is still connected.
     */
    public void ping() {
        try {
            outputStream.reset();
            SerializedAnswer answer = new SerializedAnswer();
            answer.setServerAnswer(new PingMessage());
            outputStream.writeObject(answer);
            outputStream.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            close();
        }
    }

    /**
     * Method getClientID returns the clientID of this SocketClientConnection object.
     *
     * @return the clientID (type Integer) of this SocketClientConnection object.
     */
    public Integer getClientID() {
        return clientID;
    }
}