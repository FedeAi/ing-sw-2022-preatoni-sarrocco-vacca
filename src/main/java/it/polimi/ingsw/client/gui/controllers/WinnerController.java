package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

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
//TODO button function

//    @FXML
//    public void backMenu() {
//
//    }
//    @FXML
//    public void close(){
//
//    }
}
