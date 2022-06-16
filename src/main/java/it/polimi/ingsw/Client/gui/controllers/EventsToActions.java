package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ServerMessageHandler;
import it.polimi.ingsw.Client.gui.GUI;
import javafx.application.Platform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * This class translates JavaFx events into well formatted comands to be passed to InputToMessage
 * @see it.polimi.ingsw.Client.InputToMessage
 */
public class EventsToActions implements PropertyChangeListener {
    PropertyChangeEvent currEvt = null;
    PropertyChangeEvent prevEvt = null;

    GUI gui;

    public EventsToActions(GUI gui){
        this.gui = gui;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        currEvt = evt;
        onEventReceived();
        prevEvt = evt;
    }

    private void onEventReceived() {
        System.out.println(currEvt.getPropertyName());
        String action = "";
        if(Objects.equals(currEvt.getPropertyName(), BoardController.SELECT_ASSISTANT_CARD_LISTENER)){
            action = "PLAYCARD " + String.valueOf((int)currEvt.getNewValue());
        }

        String actionToSend = action;
        Platform.runLater(()->{
            gui.getListeners().firePropertyChange("action", null, actionToSend);
        });
    }
}


