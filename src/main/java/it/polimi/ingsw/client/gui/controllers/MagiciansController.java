package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Magician;
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

/**
 * MagiciansController class represents the Magician selection scene logic.
 */
public class MagiciansController extends GUIController {

    private final EnumMap<Magician, Image> magiciansImage = new EnumMap<>(Magician.class);
    private final EnumMap<Magician, Pane> viewMap = new EnumMap<>(Magician.class);

    @FXML
    Label description; // Wizard, king, witch and sage label
    @FXML
    Pane Wizard, King, Witch, Sage;

    /**
     * Method initialize loads the magician assets and creates the image map.
     */
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

    /**
     * Method init is called at the start of the scene.¬
     */
    @Override
    public void init() {
        showMagicians();
    }

    /**
     * Method selectedMagician is called when a user selects a magician.
     * It formats the message for the server and forwards it.
     * It also changes the scene to the LOADING one.
     *
     * @param mouseEvent the MouseEvent.
     */
    @FXML
    public void selectedMagician(MouseEvent mouseEvent) {
        ImageView selection = (ImageView) mouseEvent.getSource();
        String magician = selection.getId();
        // send setup option
        String message = "MAGICIAN " + magician;
        Platform.runLater(() -> {
            gui.getListeners().firePropertyChange("action", null, message);
        });
        gui.changeScene(GUI.LOADER);
    }

    /**
     * Method showMagicians shows the remaining magicians on the scene.
     */
    public void showMagicians() {
        List<Magician> availableMagis = gui.getModelView().getAvailableMagicians();
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

    /**
     * Method showDescription shows the Magician's description when a user hovers over it.
     *
     * @param mouseEvent the MouseEvent to be checked for hovering condition.
     */
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