package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ConnectionSocket;
import it.polimi.ingsw.client.InputToMessage;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.exceptions.InvalidNicknameException;
import javafx.fxml.FXML;


import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends GUIController {

    @FXML
    private TextField username, ip, port;
    @FXML
    private Label error;

    @Override
    public void init() {
        // handle enter pressed
        // send when
        gui.getScene(GUI.LOGIN).addEventFilter(KeyEvent.ANY, (keyEvent) -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                join();
            }
        });
    }

    /**
     * Method join changes the stage scene into loading.fxml when the button "Play" is pressed.
     */
    @FXML
    public void join() {
        ip.setText("localhost");
        port.setText("8082");
        if (username.getText().equals("") || ip.getText().equals("") || port.getText().equals("")) {
            error.setText("Error: missing parameters!");
            sleepAndExec(() -> error.setText(""));
        } else if (!Constants.validateNickname(username.getText())) {
            error.setText("Error: username must be alphanumeric and contain between 3 and 15 characters.");
            sleepAndExec(() -> username.setText(""));
            sleepAndExec(() -> error.setText(""));
        } else if (!(Constants.validatePort(port.getText()) > 1023 || Constants.validatePort(port.getText()) < 65355)) {
            error.setText("Error: port should be above 1023 and under 65355");
            sleepAndExec(() -> port.setText(""));
            sleepAndExec(() -> error.setText(""));
        } else {
            gui.getModelView().setPlayerName(username.getText());
            try {
                Constants.setAddress(ip.getText());
                Constants.setPort(Integer.parseInt(port.getText()));
            } catch (NumberFormatException e) {
                error.setText(e.getMessage());
                sleepAndExec(() -> error.setText(""));
            }
            try {
                ConnectionSocket connectionSocket = new ConnectionSocket();
                if (!connectionSocket.setup(username.getText(), gui.getModelView(), gui.getServerMessageHandler())) {
                    error.setText("Server not reachable, try another IP");
                    sleepAndExec(() -> ip.setText(""));
                    sleepAndExec(() -> error.setText(""));
                    return;
                }
                gui.setConnectionSocket(connectionSocket);
                gui.getListeners().addPropertyChangeListener("action", new InputToMessage(gui.getModelView(), connectionSocket));

                error.setText("SOCKET CONNECTION \nSETUP COMPLETED!");
                sleepAndExec(() -> error.setText(""));
                gui.changeScene(GUI.LOADER);
            } catch (DuplicateNicknameException e) {
                error.setText("This nickname is already in use! Please choose another one.");
                sleepAndExec(() -> error.setText(""));
            } catch (InvalidNicknameException e) {
                error.setText("Server ERROR: Invalid character nickname");
                sleepAndExec(() -> error.setText(""));
            }
        }
    }

    @FXML
    public void clean() {
        String clean = "";
        username.setText(clean);
        ip.setText(clean);
        port.setText(clean);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setFont(font);
        ip.setFont(font);
        port.setFont(font);
        error.setFont(font);
    }
}