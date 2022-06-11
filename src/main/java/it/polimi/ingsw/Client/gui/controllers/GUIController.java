package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;

import java.util.concurrent.Callable;

public abstract class GUIController implements Initializable {

    protected Font font = Font.loadFont(getClass().getResourceAsStream("/font/PAPYRUS.ttf"), 20);

    public abstract void setGui(GUI gui);

    public void sleepAndExec(Runnable func){
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             Platform.runLater(func);

        });
        thread.start();
    }
}
