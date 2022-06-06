package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ConnectionSocket;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Constants.TowerColor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import java.awt.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SetupController implements GUIController{

    GUI gui;
    private final HashMap<Color, String> magiciansImg = new HashMap<>();
    private boolean muted;
    private String magicians = "magiciansMenu.fxml";

    @FXML
    private TextField username, ip , port;
    @FXML
    private Label error;

    //@FXML private ImageView music; TODOs

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
        }else{
            try {
                Constants.setAddress(ip.getText());
                Constants.setPort(Integer.parseInt(port.getText()));
            } catch (NumberFormatException e) {
                error.setText(e.getMessage());
                return;
            }
            gui.getModelView().setPlayerName(username.getText());

            try {
                ConnectionSocket connectionSocket = new ConnectionSocket();
                if (!connectionSocket.setup(
                        username.getText(), gui.getModelView(), gui.getServerMessageHandler())) {

                    error.setText("Server not reachable, try another IP");
                    return;
                }
                // TODo NEW SCENE
                gui.setConnectionSocket(connectionSocket);
                error.setText("SOCKET CONNECTION \nSETUP COMPLETED!");
//                loaderController.setText("WAITING FOR PLAYERS");
//                gui.getListeners()
//                        .addPropertyChangeListener(
//                                "action", new ActionParser(connectionSocket, gui.getModelView()));

            } catch (DuplicateNicknameException e) {
                error.setText("This nickname is already in use! Please choose another one.");
            } catch (InvalidNicknameException e) {
                error.setText("Server ERROR: Invalid character nickname");
            }
        }
        gui.changeScene(magicians);

    }
    @FXML
    public void clean(){
        String clean = "";
        username.setText(clean);
        ip.setText(clean);
        port.setText(clean);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
