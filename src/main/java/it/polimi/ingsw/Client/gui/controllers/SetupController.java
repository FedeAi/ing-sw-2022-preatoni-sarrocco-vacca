package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ConnectionSocket;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import javafx.fxml.FXML;

import java.awt.*;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SetupController implements GUIController{

    GUI gui;
    private final HashMap<Color, String> magiciansImg = new HashMap<>();
    private boolean muted;
    private String MAGICIANS = "/fxml/magicians.fxml";
    private String LOADING = "/fxml/setup.fxml";

    @FXML
    private TextField username, ip , port;
    @FXML
    private Label error;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method join changes the stage scene into loading.fxml when the button "Play" is pressed.
     */
    @FXML
    public void join() {

        if (username.getText().equals("") || ip.getText().equals("") || port.getText().equals("")) {
            error.setText("Error: missing all parameters!");
            clean();
            return;
        } else if (!Constants.validateNickname(username.getText())) {
            error.setText("Error: username[2-18] e no special character");
            clean();
            return;
        } else if (!Constants.validateIp(ip.getText())) {
            error.setText("Error: address should be a port >1023 and 65355 or localhost");
            clean();
            return;
        }
      /*  }else if(Constants.validatePort(port.getText(port.getText()))){

        }*/
        else {

            try {
                Constants.setAddress(ip.getText());
                Constants.setPort(Integer.parseInt(port.getText()));
            } catch (NumberFormatException e) {
                error.setText(e.getMessage());
                return;
            }
            gui.getModelView().setPlayerName(username.getText());
            LoaderController loaderController;

            try {
                gui.changeScene(LOADING);
                ConnectionSocket connectionSocket = new ConnectionSocket();
                if (!connectionSocket.setup(username.getText(), gui.getModelView(), gui.getServerMessageHandler())) {
                    error.setText("Server not reachable, try another IP");
                    gui.changeScene("m");
                    return;
                }

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
        gui.changeScene(MAGICIANS);

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
