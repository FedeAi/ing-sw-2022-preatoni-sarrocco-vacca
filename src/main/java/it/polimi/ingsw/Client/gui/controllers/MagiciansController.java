package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Magician;
import it.polimi.ingsw.Constants.TowerColor;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Exceptions.InvalidIndexException;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
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
import java.security.InvalidParameterException;
import java.util.*;

import static it.polimi.ingsw.Constants.Magician.*;

public class MagiciansController implements GUIController {


    GUI gui;
    private Game tempGame;
    private List<Pane> availableMagicians = new ArrayList<>();

    @FXML
    Label WD,K,WH,S; //wizard, king, witch and sage label

    @FXML
    Pane mago1,mago2,mago3,mago4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableMagicians.addAll(List.of(mago1,mago2,mago3,mago4));
        GameManager tempGM = new GameManager(new Game(), new GameHandler(new Server()));
        tempGM.addPlayer(new Player(0,"Davide"));
        tempGM.addPlayer(new Player(1,"ale"));
        tempGM.addPlayer(new Player(2,"fede"));
        tempGM.initGame();
        tempGame = tempGM.getGame();
        showMagicians();

    }
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }


    public void showMagicians() {
        List<Button> buttons = tempGame.getPlayers().stream().map(Player::getMagician).filter(Objects::nonNull)
                .map(p->{
                    Button btn = new Button();
                    btn.setId(p.toString());
                    btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/magicians/mago1.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
                    btn.setOnMouseClicked(this::chooseMagician);
                    btn.setOnMouseEntered(this::showDescription);
                    btn.setOnMouseExited(this::showDescription);
                    return btn;
                    }
                ).toList();

        assert buttons.size() <= availableMagicians.size();

        for(int i = 0; i< buttons.size(); i ++){
            availableMagicians.get(i).getChildren().clear();
            availableMagicians.get(i).getChildren().add(buttons.get(i));

        }

    }
    public void chooseMagician(MouseEvent event){
      System.out.println("prova");

    }
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

            K.setText("");
            WH.setText("");
            WD.setText("");
            S.setText("");
        }


        }
    }





}
