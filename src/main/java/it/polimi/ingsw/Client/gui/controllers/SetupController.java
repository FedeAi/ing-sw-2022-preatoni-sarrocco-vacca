package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Constants;
import javafx.fxml.FXML;

import java.awt.*;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SetupController implements GUIController {

    GUI gui;
    private boolean muted;

    @FXML
    private TextField username;
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Label error;
    //@FXML private ImageView music; TODO

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method play changes the stage scene to the setup one when the button "Play" is pressed.
     */
    @FXML
    public void join() {
        String errorMsg = "";

        if (!Constants.validateIp(ip.getText())) {
            errorMsg = errorMsg + "IP address";

        }
        if (Constants.validatePort(port.getText()) == -1) {
            if (errorMsg.length() > 0) {
                errorMsg = errorMsg + ",  port";
            } else {
                errorMsg = "Port";
            }
        }
        if (!Constants.validateNickname(username.getText())) {
            if (errorMsg.length() > 0) {
                errorMsg = errorMsg + ",  username";
            } else {
                errorMsg = "Username";
            }
        }
        if (!errorMsg.equals("")) {
            error.setText("Invalid fields: " + errorMsg);
        }
    }

    @FXML
    public void play() {
        gui.changeScene("setup.fxml");
    }

//    public void about() throws URISyntaxException, IOException {
//        Desktop.getDesktop().browse(new URI("https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca"));
//    }

    /**
     * Method quit kills the application when the "Quit" button is pressed.
     */
    @FXML
    public void quit() {
        System.out.println("Thanks for playing! See you next time!");
        System.exit(0);
    }


}
