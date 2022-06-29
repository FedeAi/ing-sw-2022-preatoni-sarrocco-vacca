package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.server.answers.WinMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * WinnerController class represents the Game end scene logic.
 */
public class WinnerController extends GUIController {

    @FXML
    Label display;
    @FXML
    Button menu, exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        display.setText("");
        display.setFont(font);
    }

    @FXML
    public void backMenu() {
        gui.changeScene(GUI.MENU);
    }

    @FXML
    public void close() {
        gui.stop();
    }

    public void printMessage() {
        display.setText("");
    }
}