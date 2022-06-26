package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ServerMessageHandler;
import it.polimi.ingsw.constants.GameState;
import it.polimi.ingsw.server.answers.CustomMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * LoaderController class represents the Loading scene logic.
 */
public class LoaderController extends GUIController {

    @FXML
    Label status;

    /**
     * Method initialize sets the scene's font.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        status.setFont(font);
    }

    /**
     * Method setText sets the loading scene message.
     *
     * @param status the message to be set.
     */
    public void setText(String status) {
        this.status.setText(status);
    }

    /**
     * Method propertyChange displays different status regarding different types of Messages received.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ServerMessageHandler.CUSTOM_MESSAGE_LISTENER ->
                    handleCustomMessages(((CustomMessage) evt.getNewValue()).getMessage());
            case ServerMessageHandler.GAME_STATE_LISTENER -> handleCustomMessages("");
        }
    }

    /**
     * Method handleCustomMessages sets the custom messages.
     *
     * @param msg the message to be set.
     */
    private void handleCustomMessages(String msg) {
        boolean roundOwner = gui.getModelView().amIRoundOwner();
        if (gui.getModelView().getGameState() == GameState.GAME_ROOM && !roundOwner) {
            this.status.setText(msg);
        }
        if (gui.getModelView().getGameState() == GameState.SETUP_CHOOSE_MAGICIAN && !roundOwner) {
//            this.status.setText(msg);
            this.status.setText(gui.getModelView().getRoundOwner() + " is choosing the magician");
        }

        if (gui.getModelView().getGameState() != GameState.GAME_ROOM && gui.getModelView().getGameState() != GameState.SETUP_CHOOSE_MAGICIAN) {
            this.status.setText("Waiting to be re-admitted");
        }
    }
}