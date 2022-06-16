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
    private final ArrayList<Image> magiciansImage = new ArrayList<>();
    private final ArrayList<Pane> magiciansPane = new ArrayList<>();

    @FXML
    Label description; //wizard, king, witch and sage label

    @FXML
    Pane Wizard, King, Witch, Sage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/king.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/wizard.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/witch.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("/graphics/magicians/sage.png")));
        magiciansPane.addAll(List.of(King, Wizard, Witch, Sage));
        description.setFont(font);

    }

    /**
     * this function must be called after the controller has been initialized
     */
    public void init(){
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

        // send setup option
        String message = "MAGICIAN " + magician;
        Platform.runLater(() -> {
            gui.getListeners().firePropertyChange("action", null, message);
        });
        gui.changeScene("loading.fxml");
    }

    public void showMagicians() {
        List<String> mgcns = gui.getModelView().getAvailableMagiciansStr(); //TODO check the bind from Model and scene builder
//        List<String> mgcns = List.of(new String[]{"Wizard", "King", "Witch", "Sage"});

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
            selection.setStyle("-fx-opacity: 1");
            description.setText("");
        }
    }
}






