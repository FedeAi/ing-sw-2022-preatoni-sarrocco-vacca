package it.polimi.ingsw.Client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void onExitButtonClick() {
        try {
            sleep(500);
        } catch (Exception e){
            e.getMessage();
        }
        exit(1);
    }

}