package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * WinnerController class represents the Game end scene logic.
 */
public class WinnerController extends GUIController {

    @FXML
    Label display;
    @FXML
    Button exit;

    /**
     * Method initialize sets the initial content of the scene's main label and sets its font.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        display.setText("");
        display.setFont(font);
    }

    /**
     * Method close is called when a user clicks on the button "exit" on the scene. It closes the game.
     */
    @FXML
    public void close() {
        gui.stop();
    }

    /**
     * Method printWinner prints if the current player has won or lost the game.
     *
     * @param winner the game's winner.
     */
    public void printWinner(String winner) {
        if (Objects.equals(winner, gui.getModelView().getPlayerName())) {
            display.setText("You won the game.");
        } else {
            display.setText("Player " + winner + " has won.");
        }
    }

    /**
     * Method printEnd prints when a game ends before starting. It prints the nickname of the player that has left the game.
     *
     * @param player the nickname of the player that has left the game.
     */
    public void printEnd(String player) {
        display.setText("Player" + player + " has left the game. The game has ended.");
    }
}