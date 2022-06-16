package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.InputToMessage;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MagiciansController extends GUIController {


    GUI gui;
    private final String BOARD = "board.fxml";
    private final ArrayList<Image> magiciansImage = new ArrayList<>();
    private final ArrayList<Pane> magiciansPane = new ArrayList<>();

    @FXML
    Label description; //wizard, king, witch and sage label

    @FXML
    Pane Wizard, King, Witch, Sage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/mago1.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/mago2.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/mago3.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/mago4.png")));
        magiciansPane.addAll(List.of(Wizard, King, Witch, Sage));
        description.setFont(font);
        showMagicians();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @FXML
    public void selectedMagician(MouseEvent mouseEvent) {

        HashMap OwnMag = new HashMap();
        Magician mag = null;
        ImageView selection = (ImageView) mouseEvent.getSource();
        String magician = selection.getId();

        switch(magician.toUpperCase()){
            case "WIZARD" -> mag = Magician.WIZARD;
            case "KING" -> mag = Magician.KING;
            case "WITCH" -> mag = Magician.WITCH;
            case "SAGE" -> mag = Magician.SAGE;

        }

        OwnMag.put(gui.getModelView().getPlayerName(), mag);
        gui.getModelView().setPlayerMapMagician(OwnMag);

        gui.changeScene(BOARD);

    }

    public void showMagicians() {
        ///List<String> mgcns = gui.getModelView().getAvailableMagiciansStr(); //TODO check the bind from Model and scene builder
        List<String> mgcns = List.of(new String[]{"Wizard","King", "Witch", "Sage"});

        for (int i = 0; i < mgcns.size(); i++) {
            ImageView view = new ImageView(magiciansImage.get(i));
            view.setFitHeight(404);
            view.setFitWidth(350);
            //view.setStyle("-fx-opacity: 0.3");
            view.setOnMouseClicked(this::selectedMagician);
            view.setOnMouseEntered(this::showDescription);
            view.setOnMouseExited(this::showDescription);
            view.setId(mgcns.get(i));
            magiciansPane.get(i).getChildren().clear();
            magiciansPane.get(i).getChildren().add(view);
        }
    }

    @FXML
    public void showDescription(MouseEvent mouseEvent) {
        ImageView selection = (ImageView) mouseEvent.getSource();
        String id = selection.getId();

        if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
            switch (id.toLowerCase()) {
                case "king" -> {
                    selection.setStyle("-fx-opacity: 0.4");
                    description.setText("The king of all kings!");
                }
                case "witch" -> {
                    selection.setStyle("-fx-opacity: 0.5");
                    description.setText("The most powerful witch in the world!");
                }
                case "sage" -> {
                    selection.setStyle("-fx-opacity: 0.4");
                    description.setText("The wise old formidable strategist!");
                }
                case "wizard" -> {
                    selection.setStyle("-fx-opacity: 0.4");
                    description.setText("The grand master of sorcerers!");
                }
            }
        } else {
            selection.setStyle("-fx-opacity: 100");
            description.setText("");
        }
    }
}






