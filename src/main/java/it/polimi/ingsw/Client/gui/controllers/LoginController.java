package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ConnectionSocket;
import it.polimi.ingsw.Client.InputToMessage;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.awt.*;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoginController extends GUIController{

    GUI gui;
    private final HashMap<Color, String> magiciansImg = new HashMap<>();
    private String MAGICIANS = "magicians.fxml";
    private String LOADING = "loading.fxml";
    private String MENU = "menu.fxml";
    private  String MODE = "setup.fxml";
    private String BOARD = "board.fxml";


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
            error.setText("Error: missing parameters!");
            sleepAndExec(()->error.setText(""));

        } else if (!Constants.validateNickname(username.getText())) {
            error.setText("Error: username(2-18) e no special character");
            sleepAndExec(()->username.setText(""));
            sleepAndExec(()->error.setText(""));
        } else if (!(Constants.validatePort(port.getText()) > 1023 || Constants.validatePort(port.getText()) < 65355)) {
            error.setText("Error: port should be  >1023 and  < 65355");
            sleepAndExec(()->port.setText(""));
            sleepAndExec(()->error.setText(""));
        }
        else {
            gui.getModelView().setPlayerName(username.getText());
            LoaderController loaderController;
            try {
                Constants.setAddress(ip.getText());
                Constants.setPort(Integer.parseInt(port.getText()));
            } catch (NumberFormatException e) {
                error.setText(e.getMessage());
                sleepAndExec(()->error.setText(""));
            }

            try {

                ConnectionSocket connectionSocket = new ConnectionSocket();
                int num_players = gui.getModelView().getConnectedPlayers().size();

                if (!connectionSocket.setup(username.getText(), gui.getModelView(), gui.getServerMessageHandler())) {
                    error.setText("Server not reachable, try another IP");
                    sleepAndExec(()->ip.setText(""));
                    sleepAndExec(()->error.setText(""));
                    return;
                }
                gui.setConnectionSocket(connectionSocket);

                error.setText("SOCKET CONNECTION \nSETUP COMPLETED!");
                sleepAndExec(()->error.setText(""));

                gui.getListeners().addPropertyChangeListener("action", new InputToMessage(gui.getModelView(),connectionSocket ));

                gui.changeScene(LOADING);

            } catch (DuplicateNicknameException e) {
                error.setText("This nickname is already in use! Please choose another one.");
                sleepAndExec(()->error.setText(""));

            } catch (InvalidNicknameException e) {
                error.setText("Server ERROR: Invalid character nickname");
                sleepAndExec(()->error.setText(""));
            }


        }
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
