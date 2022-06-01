package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
import it.polimi.ingsw.Client.gui.GUIController;
import it.polimi.ingsw.Constants.Color;
import it.polimi.ingsw.Constants.TowerColor;
import it.polimi.ingsw.Controller.GameManager;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Islands.Island;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.School;
import it.polimi.ingsw.Server.GameHandler;
import it.polimi.ingsw.Server.Server;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController implements GUIController {

    GUI gui;

    @FXML
    GridPane grid;

    @FXML
    Pane island0, island1, island2,island3, island4, island5,island6,island7,island8,island9,island10, island11;

    @FXML
    Pane entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9;

    private Image motherImg;
    private final ArrayList<Image> islandImgs = new ArrayList<>();
    private final HashMap<Color, Image> studentImgs = new HashMap<>();
    private final HashMap<Color, Image> profsImgs = new HashMap<>();
    private final HashMap<TowerColor, Image> towerImgs = new HashMap<>();
    private final ArrayList<Pane> islandPanes = new ArrayList<>();
    private final ArrayList<Pane> studentEntryPanes = new ArrayList<>();
    private Game tempGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        islandPanes.addAll(List.of(island0, island1, island2,island3,island4,island5,island6,island7,island8,island9,island10, island11));
        studentEntryPanes.addAll(List.of(entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9));

        loadAssets();

        GameManager tempGM = new GameManager(new Game(), new GameHandler(new Server()));
        tempGM.addPlayer(new Player(0,"a"));
        tempGM.addPlayer(new Player(1,"b"));
        tempGM.addPlayer(new Player(2,"c"));
        tempGM.initGame();
        tempGame = tempGM.getGame();

        showIslands();
        buildSchool(tempGame.getPlayers().get(0).getSchool());
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

    public void showIslands(){
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
        ImageView mother;

        StackPane pane = new StackPane();
//        pane.setPrefWidth(200);
//        pane.setPrefHeight(200);
        pane.setAlignment(Pos.CENTER);

        // island background
        ImageView background = new ImageView();
        background.setImage(islandImgs.get(index%islandImgs.size()));
        background.fitWidthProperty().bind(islandPanes.get(index).widthProperty());
        background.fitHeightProperty().bind(islandPanes.get(index).heightProperty());
        background.setSmooth(true);
        background.setCache(true);


        HBox hContainer = new HBox();
        hContainer.setAlignment( Pos.CENTER );

        // build students
        VBox students = new VBox();
        students.setAlignment( Pos.CENTER );

        for(Map.Entry<Color,Image> entry : studentImgs.entrySet()){
            if(island.getStudents().getOrDefault(entry.getKey(), 0) != 0 || true) {
                HBox hBox = new HBox();
                hBox.setAlignment( Pos.CENTER );
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

        ImageView towerImg = new ImageView(towerImgs.get(TowerColor.BLACK));
        towerImg.setFitWidth(30);
        towerImg.setPreserveRatio(true);

        Label label = new Label("2");
        Font font = new Font("System Bold", 12);
        label.setFont(font);
        tower.getChildren().addAll(towerImg, label);



        if(index == 5){
            background.setScaleX(1.5);
            background.setScaleY(1.5);
        }
        if(index == 6){
            return null;
        }

        // mother nature
        if(index == 8 || true){
            mother = new ImageView(motherImg);
            mother.setFitWidth(45);
            mother.setPreserveRatio(true);
        }

        VBox secondColumn = new VBox();
        secondColumn.setAlignment( Pos.CENTER );
        secondColumn.getChildren().add(tower);
        secondColumn.getChildren().add(mother);

        hContainer.getChildren().add(students);
        hContainer.getChildren().add(secondColumn);

        // add elements to the pane
        pane.getChildren().add(background);
        pane.getChildren().add(hContainer);
//        pane.getChildren().add(tower);

        return pane;
    }

    public void buildSchool(School school){
        // entry students
        List<Color> entryStuds = school.getStudentsEntryList();
        assert  entryStuds.size() <= studentEntryPanes.size();
        for(int i = 0; i < studentEntryPanes.size(); i++){
            studentEntryPanes.get(i).getChildren().clear(); // first remove previous items
            if(i < entryStuds.size()){
                ImageView student = new ImageView(studentImgs.get(entryStuds.get(i)));
                student.fitWidthProperty().bind(studentEntryPanes.get(i).widthProperty());
                student.fitHeightProperty().bind(studentEntryPanes.get(i).heightProperty());
                student.setSmooth(true);
                student.setCache(true);

                studentEntryPanes.get(i).getChildren().add(student);
            }
        }
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
