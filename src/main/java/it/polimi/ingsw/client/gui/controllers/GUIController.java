package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * GUIController class defines an abstract representing all the different GUI controllers.
 */
public abstract class GUIController implements Initializable, PropertyChangeListener {

    protected Font font = Font.loadFont(getClass().getResourceAsStream("/font/PAPYRUS.ttf"), 20);
    protected Font balanceFont = Font.loadFont(getClass().getResourceAsStream("/font/PAPYRUS.ttf"), 35);
    protected GUI gui;

    /**
     * Method setGui sets the GUI reference.
     *
     * @param gui the main GUI reference.
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method sleepAndExec waits and then executes a given function.
     *
     * @param func the Runnable instance to be executed.
     */
    public void sleepAndExec(Runnable func) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(func);
        });
        thread.start();
    }

    /**
     * Method init is used to initialize parameters of the different controllers.
     */
    public void init() {
    }

    /**
     * Method propertyChange is to be overrided by each controller: the different scenes have different listeners.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}