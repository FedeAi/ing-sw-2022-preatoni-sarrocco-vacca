package it.polimi.ingsw.Client.gui;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Client.gui.controllers.GUIController;
import it.polimi.ingsw.Client.gui.controllers.LoaderController;
import it.polimi.ingsw.Constants.GameState;
import it.polimi.ingsw.Server.Answer.CustomMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

//--module-path /home/federico/libs/javafx-sdk-18.0.1/lib  --add-modules javafx.controls,javafx.fxml
public class GUI extends Application implements UI {

    public static final String MENU = "menu.fxml";
    public static final String BOARD = "board.fxml";
    public static final String LOADER = "loading.fxml";
    public static final String LOGIN = "login.fxml";
    public static final String MAGIs = "magicians.fxml";
    public static final String SETUP = "setup.fxml";

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

    public GUI() {
        modelView = new ModelView(this);
//        modelView = (new ModelView(this)).readFromFile();
        serverMessageHandler = new ServerMessageHandler(this, modelView);
        activeGame = true;
    }

    /**
     * Main class of the GUI, which is called from the Eriantys launcher in case user decides to play with it.
     *
     * @param args of type String[] - parsed arguments.
     */
    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
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

    @Override
    public void stop() {
        System.exit(0);
    }

    public void setup() throws IOException {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, LOGIN, BOARD, LOADER, SETUP, MAGIs));
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

    public void run() {
        stage.setTitle("Eriantys");
        stage.centerOnScreen();
        stage.show();
    }

    public void changeScene(String newScene) {
        nameMapController.get(newScene).init();
        currentScene = nameMapScene.get(newScene);
        stage.setScene(currentScene);
        stage.centerOnScreen();
        stage.show();
    }

    public Scene getScene(String name) {
        return nameMapScene.get(name);
    }

    public void setConnectionSocket(ConnectionSocket connectionSocket) {
        if (this.connectionSocket == null) {
            this.connectionSocket = connectionSocket;
        }
    }

    public ModelView getModelView() {
        return modelView;
    }

    public ServerMessageHandler getServerMessageHandler() {
        return serverMessageHandler;
    }

    public GUIController getControllerFromName(String name) {
        return nameMapController.get(name);
    }

    public PropertyChangeSupport getListeners() {
        return listeners;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            listeners.firePropertyChange(evt);
        });
        switch (evt.getPropertyName()) {
            case ServerMessageHandler.REQ_PLAYERS_LISTENER -> Platform.runLater(() -> {
                changeScene(SETUP);
            });    // setup scene
            case ServerMessageHandler.REQ_MAGICIAN_LISTENER -> Platform.runLater(() -> {
                changeScene(MAGIs);
            });    // magician scene
            case ServerMessageHandler.PLAYERS_REJOIN_LISTENER -> {
                if (((List<String>) evt.getNewValue()).contains(getModelView().getPlayerName())) {
                    // Rejoin of the player
                    Platform.runLater(() -> {
                        changeScene(BOARD);
                    });
                }
            }
            case ServerMessageHandler.GAME_STATE_LISTENER -> {
                if (modelView.getGameState() == GameState.SETUP_CHOOSE_MAGICIAN && modelView.amIRoundOwner()) {
                    Platform.runLater(() -> {
                        changeScene(MAGIs);
                    });
                }
                // starting the play
                if (modelView.getGameState() == GameState.PLANNING_CHOOSE_CARD && modelView.getPrevGameState() == GameState.SETUP_CHOOSE_MAGICIAN) {
                    Platform.runLater(() -> {
                        changeScene(BOARD);
                    });
                }
            }

        }
    }
}