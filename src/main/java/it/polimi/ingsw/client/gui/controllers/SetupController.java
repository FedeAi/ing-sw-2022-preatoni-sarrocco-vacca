package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * SetupController class represents the Setup scene logic.
 */
public class SetupController extends GUIController {

    @FXML
    RadioButton normalGame, expertGame;
    @FXML
    Button btn2, btn3;
    @FXML
    ToggleGroup gameModeToggle;
    @FXML
    Label title, error;

    /**
     * Method check is triggered by the mouse click on the Connect GUI button.
     * It checks for the correctness of the Setup selection, and creates the related message.
     *
     * @param event the MouseEvent received from the mouse click.
     */
    @FXML
    public void check(MouseEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String expert = "";
        if (expertGame.isSelected() || normalGame.isSelected()) {
            if (expertGame.isSelected()) {
                expert = " expert";
            }
            String numPlayer = " 2";
            if (id.equalsIgnoreCase("button3")) {
                numPlayer = " 3";
            }
            gui.changeScene(GUI.LOADER);
            // If normal mode string must be empty
            String message = "SETUP" + numPlayer + expert;
            // Send setup option
            Platform.runLater(() -> {
                gui.getListeners().firePropertyChange("action", null, message);
            });
        } else {
            error.setText("Select a mode before starting!");
            sleepAndExec(() -> error.setText(""));
        }
    }

    /**
     * Method initialize initializes the scene's fonts.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn2.setFont(font);
        btn3.setFont(font);
        error.setFont(font);
        title.setFont(font);
        normalGame.setFont(font);
        expertGame.setFont(font);
    }
}