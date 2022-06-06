package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MenuController implements GUIController {
    GUI gui;

    private static final String QUIT = "quit.fxml";
    private  static final String SETUP = "setup.fxml";
    private final List<Pane> buttonEffectPanes = new ArrayList<>(); //button effect
    private final ArrayList<Image> buttonImages = new ArrayList<>(); //image of button
    private Image pergOriginal, pergLoading;

    @FXML
    Pane play, exit;

    /**
     * Method play run the setup.fxml (change of the scene) when the "Play" button is clicked.
     */
    @FXML
    public void play() {
        gui.changeScene(SETUP);
    }
    /**
     * Method about show our github when the label that contains the names is clicked
     */
    @FXML
    public void about() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca"));
    }
    /**
     * Method quit kills the application when the "Quit" button is clicked.
     */
    @FXML
    public void quit() throws InterruptedException {
        gui.changeScene(QUIT);
        TimeUnit.SECONDS.sleep(5);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonEffectPanes.addAll(List.of(play, exit));
        pergOriginal= new Image(getClass().getResourceAsStream("/graphics/perg2.png"));
        pergLoading= new Image(getClass().getResourceAsStream("/graphics/perg_clicked.png"));

        //showButtonsEffect();
    }

//    private void showButtonsEffect(){
//
//        List<Button> buttons = new ArrayList<>();
//
//        Button btnPlay = new Button();
//        btnPlay.setId("play");
//        btnPlay.setOnMouseEntered(this::changeButton);
//        btnPlay.setOnMouseExited(this::changeButton);
//
//        buttons.add(btnPlay);
//
//        Button btnExit = new Button();
//        btnExit.setId("exit");
//        btnExit.setOnMouseEntered(this::changeButton);
//        btnExit.setOnMouseExited(this::changeButton);
//
//        buttons.add(btnExit);
//
//        for(int i = 0; i< buttons.size(); i ++){
//            //buttonEffectPanes.get(i).getChildren().clear();
//            buttonEffectPanes.get(i).getChildren().add(buttons.get(i));
//        }
//    }
    public void changeButton(MouseEvent mouseEvent){
        Pane btn = (Pane) mouseEvent.getSource();
        String id = btn.getId();

        if(mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED){
            // restore normal button
            if(id.equals("play")){
                play.getChildren().clear();
                ImageView perg = new ImageView(pergOriginal);
                perg.fitWidthProperty().bind(play.widthProperty());
                perg.fitHeightProperty().bind(play.heightProperty());
                perg.setSmooth(true);
                perg.setCache(true);
                play.getChildren().add(perg);
            }
        }
        else{
            // load the new button
            if(id.equals("play")){
                play.getChildren().clear();
                ImageView perg = new ImageView(pergLoading);
                perg.fitWidthProperty().bind(play.widthProperty());
                perg.fitHeightProperty().bind(play.heightProperty());
                perg.setSmooth(true);
                perg.setCache(true);
                play.getChildren().add(perg);
            }

        }
    }

}
