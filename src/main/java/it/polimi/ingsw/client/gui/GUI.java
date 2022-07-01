package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.gui.controllers.GUIController;
import it.polimi.ingsw.client.gui.controllers.WinnerController;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.exceptions.InvalidNicknameException;
import it.polimi.ingsw.server.answers.WinMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

//--module-path /home/federico/libs/javafx-sdk-18.0.1/lib  --add-modules javafx.controls,javafx.fxml

/**
 * GUI is the main class for everything regarding the GUI client.
 * It instances the various JavaFX scenes and switches between them.
 * Scene logic is handles by various controllers.
 *
 */
public class GUI extends Application implements UI {

    public static final String MENU = "menu.fxml";
    public static final String BOARD = "board.fxml";
    public static final String LOADER = "loading.fxml";
    public static final String LOGIN = "login.fxml";
    public static final String MAGIs = "magicians.fxml";
    public static final String SETUP = "setup.fxml";
    public static final String END = "winner.fxml";

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ModelView modelView;
    private final ServerMessageHandler serverMessageHandler;
    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private Stage stage;
    private ConnectionSocket connectionSocket = null;
    private boolean activeGame;
    private Scene currentScene;

    /**
     * Maps each scene name to the effective scene object, in order to easily find it during scene changing operations.
     */
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<String, GUIController> nameMapController = new HashMap<>();

    /**
     * Constructor GUI creates a new GUI instance.
     */
    public GUI() {
        modelView = new ModelView(this);
        serverMessageHandler = new ServerMessageHandler(this, modelView);
        activeGame = true;
    }

    /**
     * Main class of the GUI, which is called from the Eriantys launcher in case user decides to play with it.
     *
     * @param args of type String[] - parsed arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method start sets the main stage and opens the window.
     *
     * @param stage the stage to be set.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        setup();
        run();
    }

    /**
     * Method stop closes the GUI.
     */
    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Method setup creates the Scene map and sets the Main Menu scene.
     */
    public void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, LOGIN, BOARD, LOADER, SETUP, MAGIs, END));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                nameMapScene.put(path, new Scene(loader.load()));
                GUIController controller = loader.getController();
                controller.setGui(this);
                listeners.addPropertyChangeListener(controller);
                nameMapController.put(path, controller);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        changeScene(LOGIN);
        //currentScene = nameMapScene.get(LOADER);
    }

    /**
     * Method run sets the window title and opens the GUI window.
     */
    public void run() {
        stage.setTitle("Eriantys");
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Method changeScene changes the stage to a new scene.
     *
     * @param newScene the scene to be set.
     */
    public void changeScene(String newScene) {
        currentScene = nameMapScene.get(newScene);
        Platform.runLater(()->{
            nameMapController.get(newScene).init();
            stage.setScene(currentScene);
            stage.centerOnScreen();
            stage.show();
        });
    }

    /**
     * Method getScene returns a scene given a scene's name.
     *
     * @param name the scene's name.
     * @return the scene's reference.
     */
    public Scene getScene(String name) {
        return nameMapScene.get(name);
    }

    /**
     * Method setConnectionSocket sets the client's current ConnectionSocket instance.
     *
     * @param connectionSocket the ConnectionSocket to be set.
     */
    public void setConnectionSocket(ConnectionSocket connectionSocket) {
        if (this.connectionSocket == null) {
            this.connectionSocket = connectionSocket;
        }
    }

    /**
     * Method getModelView returns the GUI's model reference.
     *
     * @return The GUI's model reference.
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * Method getServerMessageHandler returns the ServerMessageHandler's reference.
     *
     * @return The ServerMessageHandler's reference.
     */
    public ServerMessageHandler getServerMessageHandler() {
        return serverMessageHandler;
    }

    /**
     * Method getControllerFromName returns the reference to the scene's controller given the scene's name.
     *
     * @param name the scene's name.
     * @return The scene's controller reference.
     */
    public GUIController getControllerFromName(String name) {
        return nameMapController.get(name);
    }

    /**
     * Method getListeners returns the GUI's listeners.
     */
    public PropertyChangeSupport getListeners() {
        return listeners;
    }

    /**
     * Method propertyChange changes the GUI's scene in relation to received messages from the server.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            listeners.firePropertyChange(evt);
        });
        switch (evt.getPropertyName()) {
            // Setup scene
            case ServerMessageHandler.REQ_PLAYERS_LISTENER -> changeScene(SETUP);
            // Magician selection scene
            case ServerMessageHandler.REQ_MAGICIAN_LISTENER -> changeScene(MAGIs);

            // Magician selection scene
            case ServerMessageHandler.GAME_STATE_LISTENER -> {
                if (modelView.getGameState() == GameState.SETUP_CHOOSE_MAGICIAN && modelView.amIRoundOwner()) {
                    changeScene(MAGIs);
                }

                // Starts the main game board
                // If a players change is detected, and we are in game phase
                if (!GameState.getSetupStates().contains(modelView.getGameState()) && !modelView.getGameState().equals(GameState.GAME_ENDED)) {
                    // if not in BOARD scene --> we are in waiting to be re-admitted
                    if (currentScene != nameMapScene.get(BOARD) && currentScene != nameMapScene.get(END)) {
                        changeScene(BOARD);
                    }
                }
            }
            case ServerMessageHandler.WIN_MESSAGE_LISTENER -> {
                Platform.runLater(() -> {
                    String winner = ((WinMessage) evt.getNewValue()).getMessage();
                    ((WinnerController) getControllerFromName(END)).printWinner(winner);
                });
                changeScene(END);

            }
            case SocketListener.CONNECTION_CLOSE_LISTENER -> {
                if (!modelView.getGameState().equals(GameState.GAME_ENDED)) {
                    Platform.runLater(() -> {
                        String msg = "Connection closed";
                        ((WinnerController) getControllerFromName(END)).printMsg(msg);
                    });
                    changeScene(END);
                }
            }
        }
    }
}