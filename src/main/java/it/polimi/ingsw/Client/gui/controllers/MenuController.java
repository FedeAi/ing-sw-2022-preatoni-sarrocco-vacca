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

import static java.lang.System.exit;

public class MenuController implements GUIController {

    GUI gui;
    private  static final String SETUP = "setup.fxml";
    private  static final String QUIT = "Quit.fxml";



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
        gui.changeScene(QUIT);
        exit(1);
    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
