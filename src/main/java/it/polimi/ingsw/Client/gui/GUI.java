package it.polimi.ingsw.Client.gui;

import it.polimi.ingsw.Client.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class GUI extends Application implements UI {

    public static final String END_OF_THE_GAME = "End of the game";
    private static final String MAIN_GUI = "mainScene.fxml";
    private static final String MENU = "menu.fxml";
    private static final String LOADER = "loading.fxml";
    private static final String MAGICIANS = "magiciansMenu.fxml";
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
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        setup();
        this.stage = stage;
        //CHOOSE FONT
        //    Font.loadFont(getClass().getResourceAsStream("/fonts/DalekPinpointBold.ttf"), 14);
        //    Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"), 12);
        //    Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Medium.ttf"), 12);
        run();
    }

    // NOT NEEDED AS OF NOW
    public void setup() throws IOException {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, SETUP));
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
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/..")));
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
//            player.play();
//        });
    }
    
    public void changeScene(String newScene){
        stage.setScene(nameMapScene.get(newScene));
        stage.show();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}