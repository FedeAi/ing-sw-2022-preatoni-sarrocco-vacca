package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class MenuController extends GUIController {

    GUI gui;
    private  static final String SETUP = "setup.fxml";

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
