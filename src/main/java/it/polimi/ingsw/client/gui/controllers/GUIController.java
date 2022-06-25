package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class GUIController implements Initializable, PropertyChangeListener {

    protected Font font = Font.loadFont(getClass().getResourceAsStream("/font/PAPYRUS.ttf"), 20);
    protected Font balanceFont = Font.loadFont(getClass().getResourceAsStream("/font/PAPYRUS.ttf"), 35);
    protected GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

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
     * This method is used to initialize parameters of the controller that are not available
     * when Initializable.initialize is called. It must be called after the controller has been initialized
     */
    public void init() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
