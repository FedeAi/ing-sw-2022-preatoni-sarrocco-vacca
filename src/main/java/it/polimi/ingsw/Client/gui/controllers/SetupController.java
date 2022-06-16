package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class SetupController extends GUIController{

    private GUI gui;

    @FXML
    RadioButton NormalGame, ExpertGame;
    @FXML
    Button btn2,btn3;
    @FXML
    ToggleGroup gameModeToggle;
    @FXML
    Label title,error;




    @FXML
    public void check(MouseEvent event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String expert = "";

        if(ExpertGame.isSelected() || NormalGame.isSelected()){
            if (ExpertGame.isPressed()) {
                expert = " expert";
            }

            String numPlayer = " 2";
            if (id.equalsIgnoreCase("btn3")) {
                numPlayer = " 3";
            }

            gui.changeScene("loading.fxml");
            String message = "SETUP" + numPlayer + expert;   //if normal mode string must be empty
            // send setup option
            Platform.runLater(() -> {
                gui.getListeners().firePropertyChange("action", null, message);
            });


        }
        else{
            error.setText("choose the mode you want to play before starting");
            sleepAndExec(()->error.setText(""));
        }


    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn2.setFont(font);
        btn3.setFont(font);
        error.setFont(font);
        title.setFont(font);
        NormalGame.setFont(font);
        ExpertGame.setFont(font);


    }

}
