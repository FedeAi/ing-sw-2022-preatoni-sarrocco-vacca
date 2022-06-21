package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoaderController extends GUIController {
    @FXML
    Label status;

    @Override
    public void setGui(GUI gui) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        status.setFont(font);
    }

    public void setText(String status) {
        this.status.setText(status);
    }
}
