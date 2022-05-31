package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.ConnectionSocket;
import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.Constants;
import it.polimi.ingsw.Constants.Exceptions.DuplicateNicknameException;
import it.polimi.ingsw.Constants.Exceptions.InvalidNicknameException;
import it.polimi.ingsw.Constants.TowerColor;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController implements GUIController {

    GUI gui;

    @FXML
    GridPane grid;

    @FXML
    Pane island0, island1, island2,island3, island4, island5,island6,island7,island8,island9,island10, island11;

    private Image motherImg;
    private final ArrayList<Image> islandImgs = new ArrayList<>();
    private final HashMap<Color, Image> studentImgs = new HashMap<>();
    private final HashMap<Color, Image> profsImgs = new HashMap<>();
    private final HashMap<TowerColor, Image> towerImgs = new HashMap<>();
    private final ArrayList<Pane> islandPanes = new ArrayList<>();  //   = new ArrayList<>(List.of(island0, island1, island2,island3,island4,island5,island6,island7,island8,island9,island10, island11));

    private Game tempGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        islandPanes.addAll(List.of(island0, island1, island2,island3,island4,island5,island6,island7,island8,island9,island10, island11));

        loadAssets();

        GameManager tempGM = new GameManager(new Game(), new GameHandler(new Server()));
        tempGM.addPlayer(new Player(0,"a"));
        tempGM.addPlayer(new Player(1,"b"));
        tempGM.addPlayer(new Player(2,"c"));
        tempGM.initGame();
        tempGame = tempGM.getGame();

        showContainer();
    }

    private void loadAssets(){
        islandImgs.add( new Image(getClass().getResourceAsStream("/graphics/board/island1.png")));
        islandImgs.add( new Image(getClass().getResourceAsStream("/graphics/board/island2.png")));
        islandImgs.add( new Image(getClass().getResourceAsStream("/graphics/board/island3.png")));

        for(Color color : Color.values()){
            studentImgs.put(color, new Image(getClass().getResourceAsStream("/graphics/board/"+ color.name().toLowerCase()+"Student3D.png")));
            profsImgs.put(color, new Image(getClass().getResourceAsStream("/graphics/board/"+ color.name().toLowerCase()+"Prof3D.png")));
        }

        for(TowerColor color : TowerColor.values()){
            towerImgs.put(color, new Image(getClass().getResourceAsStream("/graphics/board/"+ color.name().toLowerCase()+"_tower.png")));
        }
        motherImg = new Image(getClass().getResourceAsStream("/graphics/board/mother_nature.png"));
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void showContainer(){
//        List<Island> islands = gui.getModelView().getIslandContainer().getIslands();
        List<Island> islands = tempGame.getIslandContainer().getIslands();

        for(int i = 0; i < islandPanes.size(); i++){
            islandPanes.get(i).getChildren().clear();
            islandPanes.get(i).setPrefHeight(Control.USE_COMPUTED_SIZE);
            islandPanes.get(i).setPrefWidth(Control.USE_COMPUTED_SIZE);
            Node newIsland = buildIsland(islands.get(i), i);
            if(newIsland!=null){
                islandPanes.get(i).getChildren().add(newIsland);
            }

        }
    }

    private Node buildIsland(Island island, int index) {
        Pane pane = new Pane();
        ImageView iv = new ImageView();
        iv.setImage(islandImgs.get(index%islandImgs.size()));
        iv.fitWidthProperty().bind(islandPanes.get(index).widthProperty());
        iv.fitHeightProperty().bind(islandPanes.get(index).heightProperty());

        pane.getChildren().add(iv);

        // build students
        VBox students = new VBox();
        students.setLayoutX(45);    // todo is there a better option?
        students.setLayoutY(30);
        students.setAlignment(Pos.BASELINE_RIGHT);
        for(Map.Entry<Color,Image> entry : studentImgs.entrySet()){
            if(island.getStudents().getOrDefault(entry.getKey(), 0) != 0 || true) {
                HBox hBox = new HBox();
                // student image
                ImageView studImg = new ImageView(entry.getValue());
                studImg.setFitWidth(15);
                studImg.setPreserveRatio(true);
                hBox.getChildren().add(studImg);
                // student label
                Label label = new Label();
                Font font = new Font("System Bold", 10);
                label.setFont(font);
                label.setText(" " + island.getStudents().getOrDefault(entry.getKey(), 0).toString());
                hBox.getChildren().add(label);

                students.getChildren().add(hBox);
            }
        }

        // tower
        HBox tower = new HBox();
        tower.setAlignment(Pos.CENTER);
        tower.setLayoutX(75);    // todo is there a better option?
        tower.setLayoutY(70);

        ImageView towerImg = new ImageView(towerImgs.get(TowerColor.BLACK));
        towerImg.setFitWidth(30);
        towerImg.setPreserveRatio(true);

        Label label = new Label("2");
        Font font = new Font("System Bold", 12);
        label.setFont(font);
        tower.getChildren().addAll(towerImg, label);



        if(index == 5){
            iv.setScaleX(1.5);
            iv.setScaleY(1.5);
        }
        if(index == 6){
            return null;
        }

        // mother nature
        if(index == 8 || true){
            ImageView mother = new ImageView(motherImg);
            mother.setFitWidth(45);
            mother.setPreserveRatio(true);
            mother.setLayoutX(70);    // todo is there a better option?
            mother.setLayoutY(25);
            pane.getChildren().add(mother);
        }


//        iv.setFitWidth(216);
//        iv.setFitHeight(216);
        iv.setSmooth(true);
        iv.setCache(true);


        pane.getChildren().add(students);
        pane.getChildren().add(tower);

        return pane;
    }


    /**
     * Method quit kills the application when the "Quit" button is pressed.
     */
    @FXML
    public void quit() {
        System.out.println("Thanks for playing! See you next time!");
        System.exit(0);
    }

}
