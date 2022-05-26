package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SetupController implements GUIController {

    GUI gui;
    private boolean muted;

    @FXML private TextField nickname;
    @FXML private TextField ip;
    @FXML private TextField port;
    @FXML private Label errorMsg;
    //@FXML private ImageView music; TODO

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /** Method play changes the stage scene to the setup one when the button "Play" is pressed. */
    public void play() {
        gui.changeScene("setup.fxml");
    }
    public void about() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/PSV-polimi-2022/ing-sw-2022-preatoni-sarrocco-vacca"));
    }
    /** Method quit kills the application when the "Quit" button is pressed. */
    public void quit() {
        System.out.println("Thanks for playing! See you next time!");
        System.exit(0);
    }


}
