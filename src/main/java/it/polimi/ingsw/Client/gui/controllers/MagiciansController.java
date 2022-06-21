package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Constants.Magician;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;
import java.util.List;

public class MagiciansController extends GUIController {

    GUI gui;
    private final EnumMap<Magician, Image> magiciansImage = new EnumMap<>(Magician.class);
    private final EnumMap<Magician, Pane> viewMap = new EnumMap<>(Magician.class);

    @FXML
    Label description; //wizard, king, witch and sage label
    @FXML
    Pane Wizard, King, Witch, Sage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Magician m : Magician.values()) {
            magiciansImage.put(m, new Image(getClass().getResourceAsStream("/graphics/magicians/" + m.toString().toLowerCase() + ".png")));
        }
        viewMap.put(Magician.KING, King);
        viewMap.put(Magician.WIZARD, Wizard);
        viewMap.put(Magician.WITCH, Witch);
        viewMap.put(Magician.SAGE, Sage);
        description.setFont(font);
    }

    @Override
    public void init() {
        showMagicians();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @FXML
    public void selectedMagician(MouseEvent mouseEvent) {
        ImageView selection = (ImageView) mouseEvent.getSource();
        String magician = selection.getId();
        // send setup option
        String message = "MAGICIAN " + magician;
        Platform.runLater(() -> {
            gui.getListeners().firePropertyChange("action", null, message);
        });
        gui.changeScene("loading.fxml");
    }

    public void showMagicians() {
        List<Magician> availableMagis = Magician.orderMagicians(gui.getModelView().getAvailableMagicians());
        for (Magician m : availableMagis) {
            ImageView view = new ImageView(magiciansImage.get(m));
            view.setFitHeight(404);
            view.setFitWidth(350);

            view.setOnMouseClicked(this::selectedMagician);
            view.setOnMouseEntered(this::showDescription);
            view.setOnMouseExited(this::showDescription);
            view.setId(m.toString());
            viewMap.get(m).getChildren().clear();
            viewMap.get(m).getChildren().add(view);
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
            selection.setStyle("-fx-opacity: 1");
            description.setText("");
        }
    }
}