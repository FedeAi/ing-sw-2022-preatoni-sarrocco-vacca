package it.polimi.ingsw.Client.gui.controllers;

import it.polimi.ingsw.Client.gui.GUI;
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
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController extends GUIController {

    GUI gui;

    @FXML
    GridPane grid;

    @FXML
    Pane island0, island1, island2,island3, island4, island5,island6,island7,island8,island9,island10, island11;

    // entry students (school)
    @FXML
    Pane entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9;

    // hall students (school)
    @FXML
    Pane hallG1, hallG2, hallG3, hallG4, hallG5, hallG6, hallG7, hallG8, hallG9, hallG10,
    hallR1, hallR2, hallR3, hallR4, hallR5, hallR6, hallR7, hallR8, hallR9, hallR10,
    hallY1, hallY2, hallY3, hallY4, hallY5, hallY6, hallY7, hallY8, hallY9, hallY10,
    hallP1, hallP2, hallP3, hallP4, hallP5, hallP6, hallP7, hallP8, hallP9, hallP10,
    hallB1, hallB2, hallB3, hallB4, hallB5, hallB6, hallB7, hallB8, hallB9, hallB10;

    // player profs
    @FXML
    Pane playerProf1, playerProf2, playerProf3, playerProf4, playerProf5;

    // swap school buttons
    @FXML
    Pane otherPlayer1Pane, otherPlayer2Pane;

    private Image motherImg;
    private final ArrayList<Image> islandImgs = new ArrayList<>();
    private final HashMap<Color, Image> studentImgs = new HashMap<>();
    private final HashMap<Color, Image> profsImgs = new HashMap<>();
    private final HashMap<TowerColor, Image> towerImgs = new HashMap<>();
    private final ArrayList<Pane> islandPanes = new ArrayList<>();
    private final ArrayList<Pane> studentEntryPanes = new ArrayList<>();
    private final EnumMap<Color, List<Pane>> colorToHallStudents = new EnumMap<Color, List<Pane>>(Color.class);
    private final EnumMap<Color, Pane> colorToPlayerProfs = new EnumMap<Color, Pane>(Color.class);
    private final List<Pane> switchPlayerPanes = new ArrayList<>(); // list of buttons to change school
    private Game tempGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        islandPanes.addAll(List.of(island0, island1, island2,island3,island4,island5,island6,island7,island8,island9,island10, island11));
        studentEntryPanes.addAll(List.of(entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8, entry9));
        // fill colorToHallStudents Map
        colorToHallStudents.put(Color.GREEN, List.of(hallG1, hallG2, hallG3, hallG4, hallG5, hallG6, hallG7, hallG8, hallG9, hallG10));
        colorToHallStudents.put(Color.RED, List.of(hallR1, hallR2, hallR3, hallR4, hallR5, hallR6, hallR7, hallR8, hallR9, hallR10));
        colorToHallStudents.put(Color.YELLOW, List.of(hallY1, hallY2, hallY3, hallY4, hallY5, hallY6, hallY7, hallY8, hallY9, hallY10));
        colorToHallStudents.put(Color.PINK, List.of(hallP1, hallP2, hallP3, hallP4, hallP5, hallP6, hallP7, hallP8, hallP9, hallP10));
        colorToHallStudents.put(Color.BLUE, List.of(hallB1, hallB2, hallB3, hallB4, hallB5, hallB6, hallB7, hallB8, hallB9, hallB10));

        colorToPlayerProfs.put(Color.GREEN, playerProf1);
        colorToPlayerProfs.put(Color.RED, playerProf2);
        colorToPlayerProfs.put(Color.YELLOW, playerProf3);
        colorToPlayerProfs.put(Color.PINK, playerProf4);
        colorToPlayerProfs.put(Color.BLUE, playerProf5);


        switchPlayerPanes.addAll(List.of(otherPlayer1Pane, otherPlayer2Pane));

        loadAssets();

        GameManager tempGM = new GameManager(new Game(), new GameHandler(new Server()));
        tempGM.addPlayer(new Player(0,"davide"));
        tempGM.addPlayer(new Player(1,"federico"));
        tempGM.addPlayer(new Player(2,"alessandro"));
        tempGM.initGame();
        tempGame = tempGM.getGame();
        // move some students to the hall just to see
        for(int i = 0; i< 10; i++){
            tempGame.getPlayers().get(0).getSchool().addStudentHall(Color.randomColor());
        }
        // set some profs just to test
        EnumMap<Color, String> profs = new EnumMap<>(Color.class);
        profs.put(Color.RED, tempGame.getPlayers().get(0).getNickname());
        profs.put(Color.BLUE, tempGame.getPlayers().get(0).getNickname());
        profs.put(Color.YELLOW, tempGame.getPlayers().get(1).getNickname());
        tempGame.setProfessors(profs);



        showChangeSchoolButtons();
        showIslands();
        Player player = tempGame.getPlayers().get(0);
        buildSchool(player.getSchool(),
                getProfsFromNickname(tempGame.getProfessors(), player.getNickname())
                 // temp function ( to be replaced when modelView is in use
        );
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

    private void showChangeSchoolButtons(){

        List<Button> buttons = tempGame.getPlayers().stream().map(Player::getNickname).filter(player -> !Objects.equals(player, tempGame.getPlayers().get(0).getNickname()))
                .map(p -> {

                            Button btn = new Button(p);
                          //  btn.setFont();
                            btn.setId(p);

                            btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/perg2.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
                            btn.setOnMouseEntered(this::changeSchool);
                            btn.setOnMouseExited(this::changeSchool);
                            return btn;
                        }
                ).toList();
        assert buttons.size() <= switchPlayerPanes.size();

        for(int i = 0; i< buttons.size(); i ++){
            switchPlayerPanes.get(i).getChildren().clear();
            switchPlayerPanes.get(i).getChildren().add(buttons.get(i));

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

    public void buildSchool(School school, List<Color> profs){
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

        // hall students

        // first remove all students in the lane
        colorToHallStudents.forEach((key, value) -> value.forEach(
                pane -> pane.getChildren().clear()
        ));

        Map<Color, Integer> hallStuds = school.getStudentsHall();
        for(Map.Entry<Color, Integer> entry : hallStuds.entrySet()){
            assert  entry.getValue() <= colorToHallStudents.get(entry.getKey()).size();
            if(entry.getValue()>0){

                // add students to the first entry.getValue positions
                colorToHallStudents.get(entry.getKey()).subList(0, entry.getValue()).forEach(pane -> {
                    ImageView student = new ImageView(studentImgs.get(entry.getKey()));
                    student.fitWidthProperty().bind(pane.widthProperty());
                    student.fitHeightProperty().bind(pane.heightProperty());
                    student.setSmooth(true);
                    student.setCache(true);

                    pane.getChildren().add(student);
                });

            }
        }

        // PROFESSORS
        // first remove all students in the lane
        colorToPlayerProfs.forEach((key, value) -> value.getChildren().clear());

        for( Color p : profs){
            ImageView prof = new ImageView(profsImgs.get(p));
            prof.fitWidthProperty().bind(colorToPlayerProfs.get(p).widthProperty());
            prof.fitHeightProperty().bind(colorToPlayerProfs.get(p).heightProperty());
            prof.setSmooth(true);
            prof.setCache(true);
            colorToPlayerProfs.get(p).getChildren().add(prof);
        }

    }

    private List<Color> getProfsFromNickname(EnumMap<Color, String> profs, String player){
        return profs.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), player)).map(Map.Entry::getKey).toList();
    }
    // CONTROLLER METHODS ( methods called directly from the fxml )

    public void changeSchool(MouseEvent event){
        Button btn = (Button) event.getSource();
        String id = btn.getId();

        if(event.getEventType() == MouseEvent.MOUSE_EXITED){
            // restore player school

            btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/perg2.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
            School school = tempGame.getPlayers().get(0).getSchool();
            buildSchool(school, getProfsFromNickname(tempGame.getProfessors(), tempGame.getPlayers().get(0).getNickname()));
        }
        else{
            btn.setStyle(" -fx-background-color: transparent; -fx-background-image: url('/graphics/perg_clicked.png'); -fx-background-size: stretch; -fx-font-size: 24px;");
            School school = tempGame.getPlayerByNickname(id).get().getSchool();
            buildSchool(school,  getProfsFromNickname(tempGame.getProfessors(), id));
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
