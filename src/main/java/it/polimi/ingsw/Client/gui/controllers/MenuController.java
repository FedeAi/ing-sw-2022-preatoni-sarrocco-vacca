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

    private  static final String SETUP = "setup.fxml";
    private final List<Pane> buttonEffectPanes = new ArrayList<>(); //button effect
    private Image pergOriginal, pergLoading;

    @FXML
    Pane play, exit, nomi;

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
    public void quit() {
        gui.changeScene("quit.fxml");

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

    }

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
             else {
                exit.getChildren().clear();
                ImageView perg = new ImageView(pergOriginal);
                perg.fitWidthProperty().bind(exit.widthProperty());
                perg.fitHeightProperty().bind(exit.heightProperty());
                perg.setSmooth(true);
                perg.setCache(true);
                exit.getChildren().add(perg);
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
            else{
                exit.getChildren().clear();
                ImageView perg = new ImageView(pergLoading);
                perg.fitWidthProperty().bind(exit.widthProperty());
                perg.fitHeightProperty().bind(exit.heightProperty());
                perg.setSmooth(true);
                perg.setCache(true);
                exit.getChildren().add(perg);
            }

        }
    }
    public void changeLabel(MouseEvent mouseEvent){

//        if(mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED){
//            nomi.getChildren().clear();
//            Label credits = new Label();
//            credits.setStyle("-fx-font-family: Papyrus; -fx-font-size: 20px; -fx-font-weight: bold;");
//            credits.setCache(true);
//            nomi.getChildren().add(credits);
//        }
//        else{
//            nomi.getChildren().clear();
//            Label credits = new Label();
//            credits.setStyle("-fx-font-family: Papyrus; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text:");
//            credits.setCache(true);
//            nomi.getChildren().add(credits);
//        }
    }

}
