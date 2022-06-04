package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MenuController implements GUIController {

    private static final String QUIT = "quit.fxml";
    private  static final String SETUP = "setup.fxml";
    GUI gui;

    @FXML
    private Button button;
    @FXML
    private Label nomi;

    @FXML
    public void play() {
        gui.changeScene(SETUP);
    }
    public void about() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca"));
    }
    /**
     * Method quit kills the application when the "Quit" button is pressed.
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

    }
}
