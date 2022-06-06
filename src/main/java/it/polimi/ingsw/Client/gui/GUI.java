package it.polimi.ingsw.Client.gui;

import it.polimi.ingsw.Client.*;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.Answer.Answer;
import it.polimi.ingsw.Server.Answer.modelUpdate.ModelMessage;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

//--module-path /home/federico/libs/javafx-sdk-18.0.1/lib  --add-modules javafx.controls,javafx.fxml
public class GUI extends Application implements UI{

    public static final String END_OF_THE_GAME = "End of the game";
    private static final String MAIN_GUI = "mainScene.fxml";
    private static final String MENU = "menu.fxml";
    private static final String QUIT = "Quit.fxml"; //FIXME
    private static final String BOARD = "board.fxml";
    private static final String LOADER = "loading.fxml";
    private static final String SETUP = "setup.fxml";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ModelView modelView;
    private final ServerMessageHandler serverMessageHandler;

    private Stage stage;
    private ConnectionSocket connectionSocket = null;
    private boolean activeGame;
    private Scene currentScene;



    /**
     * Maps each scene name to the effective scene object, in order to easily find it during scene changing operations.
     */
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<String, GUIController> nameMapController = new HashMap<>();

    public GUI(){
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
        System.setProperty("prism.allowhidpi", "false");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.stage = stage;
        //CHOOSE FONT
        Font.loadFont(getClass().getResourceAsStream("/font/GhoustOutline.otf"), 16);
        run();
    }

    // NOT NEEDED AS OF NOW
    public void setup() throws IOException {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, SETUP, BOARD));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                nameMapScene.put(path, new Scene(loader.load()));
                GUIController controller = loader.getController();
                controller.setGui(this);
                nameMapController.put(path, controller);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        currentScene = nameMapScene.get(MENU);
    }
    public void run() {
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.centerOnScreen();
        stage.show();
//        ResizeHandler resize = new ResizeHandler((Pane) currentScene.lookup("#mainPane"));
//        currentScene.widthProperty().addListener(resize.getWidthListener());
//        currentScene.heightProperty().addListener(resize.getHeightListener());
//        Media pick = new Media(Objects.requireNonNull(getClass().getClassLoader()
//                .getResource("media/Epic_Battle_Speech.mp3")).toExternalForm());
//        player = new MediaPlayer(pick);
//        player.setAutoPlay(true);
//        player.setCycleCount(MediaPlayer.INDEFINITE);
//        player.setVolume(25);
//        player.setOnEndOfMedia(() -> {
//            player.seek(Duration.ZERO);
//            player.pla9y();
//        });
    }
    
    public void changeScene(String newScene){
        stage.setScene(nameMapScene.get(newScene));
        stage.centerOnScreen();
        stage.show();
    }

    public void setConnectionSocket(ConnectionSocket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    public ModelView getModelView(){
        return modelView;
    }

    public ServerMessageHandler getServerMessageHandler(){
        return serverMessageHandler;
    }

    private void handleModelChange(){
        Platform.runLater(() -> {
            Answer message = modelView.getServerAnswer();
            if(message instanceof ModelMessage){

            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ServerMessageHandler.GENERERIC_MODEL_UPDATE_LISTENER -> handleModelChange();
        }
    }

}