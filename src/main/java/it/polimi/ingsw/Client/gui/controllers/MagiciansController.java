package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
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
    private Game tempGame;
    private final ArrayList<Image> magiciansImage = new ArrayList<>();
    private final ArrayList<Pane> magiciansPane = new ArrayList<>();

    @FXML
    Label WD,K,WH,S; //wizard, king, witch and sage label

    @FXML
    Pane Wizard,King,Witch,Sage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        magiciansImage.add(new Image(getClass().getResourceAsStream("graphics/magicians/mago1.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("graphics/magicians/mago2.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("graphics/magicians/mago3.png")));
        magiciansImage.add(new Image(getClass().getResourceAsStream("graphics/magicians/mago4.png")));
        magiciansPane.addAll(List.of(Wizard,King,Witch,Sage));
        showMagicians();

    }
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @FXML
    public void selectedMagician(MouseEvent mouseEvent){
        System.out.println("ciao");
    }
    public void showMagicians() {


        ///List<String> mgcns = gui.getModelView().getAvailableMagiciansStr(); //TODO check the bind from Model and scene builder
        List<String> mgcns = List.of(new String[]{"Wizard", "King"});

        for(int i = 0; i< mgcns.size(); i++){

            ImageView view = new ImageView(magiciansImage.get(i));
            view.setFitHeight(175);
            view.setFitWidth(200);
            view.setOnMouseClicked(this::selectedMagician);
            view.setOnMouseEntered(this::showDescription);
            view.setOnMouseExited(this::showDescription);
            magiciansPane.get(i).getChildren().clear();
            magiciansPane.get(i).getChildren().add(view);
        }


    }
    @FXML
    public void showDescription(MouseEvent mouseEvent){

        Button btn = (Button) mouseEvent.getSource();
        String id = btn.getId();

        if(mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED){
            // restore player school
            switch (id.toLowerCase()) {
                case "king" -> {
                    K.setText("the king of all kings! He can conquer all empires and masterfully vanquish all his opponents");
                }
                case "witch" -> {
                    WH.setText("The most powerful witch in the world will be able to enchant you and convince you to give up! Always be wary");
                }
                case "sage"->{
                    S.setText("the wise old man is a formidable strategist and a weighted character between brains and brawn! ");
                }
                case "wizard"->{
                    WD.setText("The grand master of sorcerers is extraordinarily powerful, once across the multiverse to win a challenge! ");
                }
            }

        }
           else{

            sleepAndExec(()->K.setText(""));
            sleepAndExec(()->WH.setText(""));
            sleepAndExec(()->WD.setText(""));
            sleepAndExec(()->S.setText(""));

         }


        }
}






