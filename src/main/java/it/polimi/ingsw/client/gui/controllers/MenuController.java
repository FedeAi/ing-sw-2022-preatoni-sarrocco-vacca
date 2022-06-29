package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

/**
 * MenuController represents the Main Menu scene logic.
 */
public class MenuController extends GUIController {

    /**
     * Method play changes to scene to the LOGIN scene  when the "Play" button is clicked.
     */
    @FXML
    public void play() {
        gui.changeScene(GUI.LOGIN);
    }

    /**
     * Method about shows the project's GitHub repo when the label that contains the names is clicked.
     */
    @FXML
    public void about() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca"));
    }

    /**
     * Method quit kills the application when the "Exit" button is clicked.
     */
    @FXML
    public void quit() {
        exit(1);
    }

    /**
     * Method initialize is not implemented in the main menu, as every resource is statically defined in the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}