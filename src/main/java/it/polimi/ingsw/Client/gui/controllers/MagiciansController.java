package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Constants.TowerColor;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.URL;
import java.util.*;

public class MagiciansController implements GUIController {

    GUI gui;
    private final ArrayList<Image> magiciansImg = new ArrayList<>();


   // @FXML
   // Button wizard,king,witch,sage; //tjis button triggered the selection
    @FXML
    Label WD,K,WH,S; //wizard, king, witch and sage label
    @FXML
    Pane mago1,mago2,mago3,mago4;
    @FXML
    HBox place;

    private final List<Pane> availableMagicians = new ArrayList<>();

    public void showMagicians() {
        List<String> mgcns = gui.getModelView().getAvailableMagiciansStr();

        for (int i = 0; i < mgcns.size(); i++) {

            ImageView img = new ImageView();
            img.setImage(magiciansImg.get(i));
            img.fitWidthProperty().bind(availableMagicians.get(i).widthProperty());
            img.fitHeightProperty().bind(availableMagicians.get(i).heightProperty());
            img.setSmooth(true);
            img.setCache(true);
            img.setId(mgcns.get(i));
            availableMagicians.get(i).getChildren().add(img);
           // place.getChildren().add(img);

        }

    }

    @Override
    public void setGui(GUI gui) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableMagicians.addAll(List.of(mago1,mago2,mago3,mago4));
        loadAssets();
        showMagicians();

    }

    private void loadAssets() {
        magiciansImg.add( new Image(getClass().getResourceAsStream("/graphics/magicians/mago1.png")));
        magiciansImg.add( new Image(getClass().getResourceAsStream("/graphics/magicians/mago2.png")));
        magiciansImg.add( new Image(getClass().getResourceAsStream("/graphics/magicians/mago3.png")));
        magiciansImg.add( new Image(getClass().getResourceAsStream("/graphics/magicians/mago4.png")));
    }

}
